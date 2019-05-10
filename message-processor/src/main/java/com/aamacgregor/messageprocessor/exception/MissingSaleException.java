package com.aamacgregor.messageprocessor.exception;

public class MissingSaleException extends RuntimeException {
    public MissingSaleException() {
        super("The sale is missing");
    }
}
