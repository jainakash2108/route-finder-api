package no.route.finder.routefinderapi.exception;

public class SessionAlreadyStartedException extends RuntimeException {
    public SessionAlreadyStartedException(String message) {
        super(message);
    }
}
