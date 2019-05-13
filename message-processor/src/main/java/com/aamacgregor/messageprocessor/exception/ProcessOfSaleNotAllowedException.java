package com.aamacgregor.messageprocessor.exception;

public class ProcessOfSaleNotAllowedException extends RuntimeException {

    public ProcessOfSaleNotAllowedException() {
        super("The sale cannot be processed because the message limit has been reached");
    }
}
