package com.aamacgregor.messageprocessor.exception;

public class ColumnWidthMismatchException extends RuntimeException {

    public ColumnWidthMismatchException() {
        super("The number of data columns does not match the number header columns");
    }
}
