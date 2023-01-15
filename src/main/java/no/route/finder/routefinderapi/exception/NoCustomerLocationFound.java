package no.route.finder.routefinderapi.exception;

public class NoCustomerLocationFound extends RuntimeException {
    public NoCustomerLocationFound(String message) {
        super(message);
    }
}
