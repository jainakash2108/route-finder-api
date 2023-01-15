package no.route.finder.routefinderapi.exception;

public class CsvFileNotFoundException extends RuntimeException {
    public CsvFileNotFoundException(String message) {
        super(message);
    }
}
