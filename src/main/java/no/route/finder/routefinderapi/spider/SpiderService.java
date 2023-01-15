package no.route.finder.routefinderapi.spider;

import no.route.finder.routefinderapi.domain.CustomerLocation;
import no.route.finder.routefinderapi.domain.Location;
import no.route.finder.routefinderapi.domain.RouteDetails;
import no.route.finder.routefinderapi.domain.Session;
import no.route.finder.routefinderapi.domain.UnservicedOrder;
import no.route.finder.routefinderapi.exception.NoCustomerLocationFound;
import no.route.finder.routefinderapi.exception.NoOptimizationRunning;
import no.route.finder.routefinderapi.exception.NoSessionFound;
import no.route.finder.routefinderapi.exception.OptimizationAlreadyRunningException;
import no.route.finder.routefinderapi.repository.customerlocation.CustomerLocationDao;
import no.route.finder.routefinderapi.spider.request.CreateSessionParameters;
import no.route.finder.routefinderapi.spider.request.Topology;
import no.route.finder.routefinderapi.spider.request.TopologyType;
import no.route.finder.routefinderapi.spider.request.Vehicle;
import no.route.finder.routefinderapi.spider.request.Vrp;
import no.route.finder.routefinderapi.spider.response.Route;
import no.route.finder.routefinderapi.spider.response.SessionStatus;
import no.route.finder.routefinderapi.spider.response.VrpSolution;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

@Service
public class SpiderService {
    private final SpiderClient spiderClient;
    private final CustomerLocationDao customerLocationDao;

    public SpiderService(SpiderClient spiderClient, CustomerLocationDao customerLocationDao) {
        this.spiderClient = spiderClient;
        this.customerLocationDao = customerLocationDao;
    }

    public List<Session> getSessions() {
        List<String> sessionIds = customerLocationDao.selectAllSessionId();
        return sessionIds.stream().map(sessionId -> new Session(sessionId, mapToLocation(customerLocationDao.selectBySessionId(sessionId)))).collect(Collectors.toList());
    }

    public void startSession(String sessionId) {
        List<CustomerLocation> locations = customerLocationDao.selectBySessionId(sessionId);
        if (locations.isEmpty()) {
            throw new NoCustomerLocationFound("Customer locations are missing, please upload file with customer locations again!!");
        }
        List<Vehicle> vehicles = mapToVehicle(locations);
        Vrp vrp = new Vrp(new Topology(TopologyType.geographic), vehicles, emptyList());
        CreateSessionParameters createSession = new CreateSessionParameters(sessionId, vrp);
        spiderClient.startSession(createSession);
    }

    @Transactional
    public void stopSession(String sessionId) {
        spiderClient.checkSessionStatus(sessionId);
        customerLocationDao.deleteBySessionId(sessionId);
        spiderClient.stopSession(sessionId);
    }

    public void startOptimization(String sessionId, Integer threads) {
        SessionStatus sessionStatus = spiderClient.checkSessionStatus(sessionId);
        if (!sessionStatus.isReady()) {
            throw new NoSessionFound("Session is not yet ready for session id " + sessionId);
        }
        if (sessionStatus.optimizationIsRunning()) {
            throw new OptimizationAlreadyRunningException("Session is already started for session id " + sessionId);
        }
        spiderClient.startOptimization(sessionId, threads);
    }

    public void stopOptimization(String sessionId) {
        SessionStatus sessionStatus = spiderClient.checkSessionStatus(sessionId);
        if (sessionStatus.optimizationIsRunning()) {
            spiderClient.stopOptimization(sessionId);
        } else {
            throw new NoOptimizationRunning("Session is already stopped for session id " + sessionId);
        }
    }

    public RouteDetails bestRoutes(String sessionId) {
        SessionStatus sessionStatus = spiderClient.checkSessionStatus(sessionId);
        if (sessionStatus.isReady() && sessionStatus.optimizationIsRunning()) {
            VrpSolution vrpSolution = spiderClient.findBestSolution(sessionId);
            return new RouteDetails(vrpSolution.routes(), mapToUnservicedOrder(sessionId, vrpSolution.unservicedOrderIds()));
        } else {
            throw new NoOptimizationRunning("Session is not yet ready or session is not running for session id " + sessionId);
        }
    }

    public Route bestRoutes(String sessionId, String vehicleId) {
        SessionStatus sessionStatus = spiderClient.checkSessionStatus(sessionId);
        if (sessionStatus.isReady() && sessionStatus.optimizationIsRunning()) {
            return spiderClient.findBestSolution(sessionId, vehicleId);
        } else {
            throw new NoOptimizationRunning("Session is not yet ready or session is not running for session id " + sessionId);
        }
    }

    private List<Location> mapToLocation(List<CustomerLocation> customerLocations) {
        return customerLocations.stream().map(customerLocation -> new Location(customerLocation.vehicleId(), customerLocation.latitude(), customerLocation.longitude())).collect(Collectors.toList());
    }

    private List<Vehicle> mapToVehicle(List<CustomerLocation> locations) {
        return locations.stream().map(location -> new Vehicle(location.vehicleId(), startAddress(location))).collect(Collectors.toList());
    }

    private List<UnservicedOrder> mapToUnservicedOrder(String sessionId, List<String> orderIds) {
        return orderIds.stream().map(orderId -> new UnservicedOrder(orderId, spiderClient.findUnservicedReason(sessionId, orderId).description())).collect(Collectors.toList());
    }

    private String startAddress(CustomerLocation location) {
        //sample: "lat=56.8312000436932;lon=13.9368456981301",
        return "lat=" + location.latitude() + ";" + "lon=" + location.longitude();
    }

}
