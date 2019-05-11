package com.aamacgregor.messageprocessor.exception;

public class InvalidProductNameException extends RuntimeException {
    public InvalidProductNameException() {
        super("The name of the product is invalid (Can only contain alphanumerical characters and spaces)");
    }
}
