package no.route.finder.routefinderapi.exception;

public class NoSessionFound extends RuntimeException {
    public NoSessionFound(String message) {
        super(message);
    }
}
