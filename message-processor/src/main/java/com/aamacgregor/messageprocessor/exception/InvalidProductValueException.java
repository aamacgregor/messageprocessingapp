package com.aamacgregor.messageprocessor.exception;

public class InvalidProductValueException extends RuntimeException {
    public InvalidProductValueException() {
        super("The value of the product is invalid (must be greater than or equal to zero)");
    }
}
