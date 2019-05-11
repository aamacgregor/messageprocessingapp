package com.aamacgregor.messageprocessor.exception;

public class MissingAdjustmentValueException extends RuntimeException {
    public MissingAdjustmentValueException() {
        super("The value of the adjustment is missing");
    }
}
