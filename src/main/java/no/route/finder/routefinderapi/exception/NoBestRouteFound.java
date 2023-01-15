package no.route.finder.routefinderapi.exception;

public class NoBestRouteFound extends RuntimeException {
    public NoBestRouteFound(String message) {
        super(message);
    }
}
