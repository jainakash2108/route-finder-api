package no.route.finder.routefinderapi.exception;

public class OptimizationAlreadyRunningException extends RuntimeException {
    public OptimizationAlreadyRunningException(String message) {
        super(message);
    }
}
