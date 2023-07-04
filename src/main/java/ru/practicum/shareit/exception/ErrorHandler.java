package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final ValidationException e) {
        String errorMessage = "ValidationException: " + e.getMessage();
        return new ErrorResponse(errorMessage);
    }

    @ExceptionHandler
    @ResponseStatus (HttpStatus.FORBIDDEN)
    public ErrorResponse handleForbiddenException(final ForbiddenException e) {
        String errorMessage = "ForbiddenException: " + e.getMessage();
        return new ErrorResponse(errorMessage);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final NotFoundException e) {
        String errorMessage = "NotFoundException: " + e.getMessage();
        return new ErrorResponse(errorMessage);
    }

    @ExceptionHandler
    @ResponseStatus (HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleExceptionForUnsupport(final ExceptionForUnsupport e) {
        String strError = e.getMessage();
        return new ErrorResponse(strError);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        String strError = e.getMessage();
        return new ErrorResponse(strError);
    }
}
