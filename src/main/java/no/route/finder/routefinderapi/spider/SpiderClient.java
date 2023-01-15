package no.route.finder.routefinderapi.spider;

import no.route.finder.routefinderapi.config.SpiderConfig;
import no.route.finder.routefinderapi.exception.NoBestRouteFound;
import no.route.finder.routefinderapi.exception.NoOptimizationRunning;
import no.route.finder.routefinderapi.exception.NoServiceReasonFound;
import no.route.finder.routefinderapi.exception.NoSessionFound;
import no.route.finder.routefinderapi.exception.NoSessionRegisteredException;
import no.route.finder.routefinderapi.spider.request.CreateSessionParameters;
import no.route.finder.routefinderapi.spider.response.Route;
import no.route.finder.routefinderapi.spider.response.ServerStatus;
import no.route.finder.routefinderapi.spider.response.SessionStatus;
import no.route.finder.routefinderapi.spider.response.UnservicedReason;
import no.route.finder.routefinderapi.spider.response.VrpSolution;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;

@Service
public class SpiderClient {

    private final WebClient webClient;

    public SpiderClient(SpiderConfig spiderConfig) {
        webClient = WebClient
                .builder()
                .baseUrl(spiderConfig.url())
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.TEXT_PLAIN_VALUE)
                .build();
    }

    public List<String> getServer() {
        ServerStatus serverStatus = webClient
                .get()
                .uri("/server")
                .exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.bodyToMono(ServerStatus.class);
                    } else {
                        throw new NoSessionRegisteredException("No session registered");
                    }
                })
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(2)))
                .block();
        if (serverStatus == null) {
            throw new NoSessionRegisteredException("No session registered");
        } else return serverStatus.sessionIds();
    }

    public void startSession(CreateSessionParameters createSession) {
        SessionStatus sessionStatus = webClient
                .post()
                .uri("/sessions")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(createSession), CreateSessionParameters.class)
                .exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.CREATED)) {
                        return response.bodyToMono(SessionStatus.class);
                    } else {
                        throw new NoSessionRegisteredException("Session is not registered!!");
                    }
                })
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(2)))
                .block();
        if (sessionStatus == null) {
            throw new NoSessionRegisteredException("Session is not registered!!");
        }
    }

    public SessionStatus checkSessionStatus(String sessionId) {
        try {
            SessionStatus sessionStatus = webClient
                    .get()
                    .uri("/sessions/{sessionId}", sessionId)
                    .exchangeToMono(response -> {
                        if (response.statusCode().equals(HttpStatus.OK)) {
                            return response.bodyToMono(SessionStatus.class);
                        } else {
                            throw new NoSessionRegisteredException("Session is not registered for session id " + sessionId);
                        }
                    })
                    .retry(3)
                    .block();
            if (sessionStatus == null) {
                throw new NoSessionRegisteredException("Session is not registered for session id " + sessionId);
            } else return sessionStatus;
        } catch (Exception ex) {
            throw new NoSessionRegisteredException("Session is not registered for session id " + sessionId);
        }
    }

    public void stopSession(String sessionId) {
        webClient
                .delete()
                .uri("/sessions/{sessionId}", sessionId)
                .exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.releaseBody();
                    } else {
                        throw new NoSessionFound("No session found for session id" + sessionId);
                    }
                })
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(2)))
                .block();
    }

    public void startOptimization(String sessionId, Integer thread) {
        webClient
                .post()
                .uri(uriBuilder -> uriBuilder.path("/sessions/{sessionId}/startOptimization")
                        .queryParam("threadCount", thread)
                        .build(sessionId))
                .exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.releaseBody();
                    } else {
                        throw new NoSessionFound("No session found for session id" + sessionId);
                    }
                })
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(2)))
                .block();
    }

    public void stopOptimization(String sessionId) {
        webClient
                .post()
                .uri("/sessions/{sessionId}/stopOptimization", sessionId)
                .exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.releaseBody();
                    } else {
                        throw new NoOptimizationRunning("No optimization is running for session id" + sessionId);
                    }
                })
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(2)))
                .block();
    }

    public VrpSolution findBestSolution(String sessionId) {
        VrpSolution vrpSolution = webClient
                .get()
                .uri("/sessions/{sessionId}/bestSolution", sessionId)
                .exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.bodyToMono(VrpSolution.class);
                    } else {
                        throw new NoBestRouteFound("No best route found for session id " + sessionId);
                    }
                })
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(2)))
                .block();
        if (vrpSolution == null) {
            throw new NoBestRouteFound("No best route found for session id " + sessionId);
        } else return vrpSolution;
    }

    public Route findBestSolution(String sessionId, String vehicleId) {
        Route route = webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/sessions/{sessionId}/bestSolution/routes/{vehicleId}")
                        .queryParam("geometries", "geoJson")
                        .build(sessionId, vehicleId)
                )
                .exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.bodyToMono(Route.class);
                    } else {
                        throw new NoBestRouteFound("No best route found for session id " + sessionId + " and for vehicle id " + vehicleId);
                    }
                })
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(5)))
                .block();
        if (route == null) {
            throw new NoBestRouteFound("No best route found for session id " + sessionId + " and for vehicle id " + vehicleId);
        } else return route;
    }

    public UnservicedReason findUnservicedReason(String sessionId, String orderId) {
        UnservicedReason unservicedReason = webClient
                .get()
                .uri("/sessions/{sessionId}/unservicedReason/routes/{orderId}", sessionId, orderId)
                .exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.bodyToMono(UnservicedReason.class);
                    } else {
                        throw new NoServiceReasonFound("No service found for session id " + sessionId + " and for order id " + orderId);
                    }
                })
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(2)))
                .block();
        if (unservicedReason == null) {
            throw new NoServiceReasonFound("No service reason found for session id " + sessionId + " and for order id " + orderId);
        } else return unservicedReason;
    }

}
