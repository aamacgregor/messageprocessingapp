package com.aamacgregor.messageprocessor.exception;

public class InvalidAdjustmentValueException extends RuntimeException {
    public InvalidAdjustmentValueException() {
        super("The value of the adjustment must be greater than zero");
    }
}
