package com.aamacgregor.messageprocessor.controller.advice;

import com.aamacgregor.messageprocessor.exception.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Converts exceptions to the relevant HTTP Status and includes the exception message in the response body.
 * The exception message is always a simple message as can be found in the exception definition
 */
@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {InvalidProductNameException.class, InvalidProductValueException.class,
            InvalidQuantityException.class, InvalidAdjustmentValueException.class, MissingProductException.class,
            MissingProductValueException.class, MissingQuantityException.class, MissingSaleException.class,
            MissingAdjustmentValueException.class, MissingAdjustmentOperationException.class})
    protected ResponseEntity<Object> handleInvalidInputException(
            RuntimeException ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.EXPECTATION_FAILED, request);
    }

    @ExceptionHandler(value = {ProcessOfSaleNotAllowedException.class,
            ProcessOfSaleAdjustmentNotAllowedException.class})
    protected ResponseEntity<Object> handleUnableToProcessException(
            RuntimeException ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

}
