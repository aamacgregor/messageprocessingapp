package com.aamacgregor.messageprocessor.exception;

public class MissingQuantityException extends RuntimeException {
    public MissingQuantityException() {
        super("The quantity is missing");
    }
}
