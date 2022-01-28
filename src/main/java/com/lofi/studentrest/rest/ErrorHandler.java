package com.lofi.studentrest.rest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;

import javax.validation.ConstraintViolationException;
import java.io.IOException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * ErrorHandler to convert 500 error to 400 when ConstraintViolationException is occurs
 */
@ControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    public void handleConstraintViolationException(ConstraintViolationException exception, ServletWebRequest webRequest) throws IOException {
        var response = webRequest.getResponse();
        if (response != null) {
            response.sendError(BAD_REQUEST.value(), exception.getMessage());
        }
    }
}
