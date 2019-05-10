package com.aamacgregor.messageprocessor.advice;

import com.aamacgregor.messageprocessor.exception.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {InvalidProductValueException.class, InvalidQuantityException.class,
            MissingProductException.class, MissingProductValueException.class,
            MissingQuantityException.class, MissingSaleException.class})
    protected ResponseEntity<Object> handleInvalidInputException(
            RuntimeException ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.EXPECTATION_FAILED, request);
    }

}
