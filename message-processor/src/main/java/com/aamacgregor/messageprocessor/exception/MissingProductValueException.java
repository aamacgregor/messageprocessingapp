package com.aamacgregor.messageprocessor.exception;

public class MissingProductValueException extends RuntimeException {
    public MissingProductValueException() {
        super("The value of the product is missing");
    }
}
