package com.aamacgregor.messageprocessor.exception;

public class InvalidQuantityException extends RuntimeException {
    public InvalidQuantityException() {
        super("The quantity is invalid (must be greater than one)");
    }
}
