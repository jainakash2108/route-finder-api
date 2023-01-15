package no.route.finder.routefinderapi.exception;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.slf4j.LoggerFactory.getLogger;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOG = getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({CsvFileNotFoundException.class, IllegalParamException.class, OptimizationAlreadyRunningException.class, SessionAlreadyStartedException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleCsvFileNotFoundException(RuntimeException ex) {
        LOG.error("Error message : {}", ex.getMessage());
        return new ErrorMessage(ex.getMessage());
    }

    @ExceptionHandler({NoSessionFound.class, NoBestRouteFound.class, NoOptimizationRunning.class, NoServiceReasonFound.class, NoSessionRegisteredException.class, NoCustomerLocationFound.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleNoServerFound(RuntimeException ex) {
        LOG.error("Error message : {}", ex.getMessage());
        return new ErrorMessage(ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handleRuntimeException(RuntimeException ex) {
        LOG.error("Error message : {}", ex.getMessage());
        return new ErrorMessage(ex.getMessage());
    }
}
