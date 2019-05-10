package com.aamacgregor.messageprocessor.exception;

public class MissingProductException extends RuntimeException {
    public MissingProductException() {
        super("The product name is missing");
    }
}
