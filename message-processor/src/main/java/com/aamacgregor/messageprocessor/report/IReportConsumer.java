package com.aamacgregor.messageprocessor.report;

public interface IReportConsumer {

    /**
     * Consumes the report
     *
     * @param report the report to report
     */
    void consume(String report);
}
