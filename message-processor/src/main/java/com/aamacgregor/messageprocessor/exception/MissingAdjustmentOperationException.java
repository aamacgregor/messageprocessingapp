package com.aamacgregor.messageprocessor.exception;

public class MissingAdjustmentOperationException extends RuntimeException {
    public MissingAdjustmentOperationException() {
        super("The adjustment operation is missing");
    }
}
