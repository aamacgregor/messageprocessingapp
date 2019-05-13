package com.aamacgregor.messageprocessor.exception;

public class ProcessOfSaleAdjustmentNotAllowedException extends RuntimeException {

    public ProcessOfSaleAdjustmentNotAllowedException() {
        super("The sale adjustment cannot be processed because the message limit has been reached");
    }
}
