package no.route.finder.routefinderapi.controller;

import no.route.finder.routefinderapi.exception.IllegalParamException;
import no.route.finder.routefinderapi.spider.SpiderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/sessions")
public class RouteFinderController {

    private final SpiderService spiderService;

    public RouteFinderController(SpiderService spiderService) {
        this.spiderService = spiderService;
    }

    @GetMapping
    public ResponseEntity getSessions() {
        return ok().body(spiderService.getSessions());
    }

    @PostMapping("/{sessionId}/register")
    public ResponseEntity registerSession(@PathVariable String sessionId) {
        spiderService.startSession(sessionId);
        return ok().build();
    }

    @PostMapping("/{sessionId}/unregister")
    public ResponseEntity unregisterSession(@PathVariable String sessionId) {
        spiderService.stopSession(sessionId);
        return ok().build();
    }

    @PostMapping("/{sessionId}/start")
    public ResponseEntity startSession(@PathVariable String sessionId, @RequestParam(required = false) Integer threads) {
        if (threads != null && threads == 0) {
            throw new IllegalParamException("threads should be greater than 1");
        }
        if (threads == null) {
            threads = 1;
        }
        spiderService.startOptimization(sessionId, threads);
        return ok().build();
    }

    @PostMapping("/{sessionId}/stop")
    public ResponseEntity stopSession(@PathVariable String sessionId) {
        spiderService.stopOptimization(sessionId);
        return ok().build();
    }

    @GetMapping("/{sessionId}/findRoutes")
    public ResponseEntity findRoutes(@PathVariable String sessionId) {
        return ok().body(spiderService.bestRoutes(sessionId));
    }

    @GetMapping("/{sessionId}/vehicles/{vehicleId}/findBestRoutes")
    public ResponseEntity findRoutes(@PathVariable String sessionId, @PathVariable String vehicleId) {
        return ok().body(spiderService.bestRoutes(sessionId, vehicleId));
    }

}
