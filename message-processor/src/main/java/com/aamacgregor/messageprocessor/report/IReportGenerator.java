package com.aamacgregor.messageprocessor.report;

public interface IReportGenerator {

    /**
     * Returns the interval at which the table should be generated. i.e. the value 10 would mean that the
     * report should be generated every 10 messages.
     *
     * @return
     */
    int getScheduleInterval();


    /**
     * Generates the report table and hands it to the report consumer.
     */
    void generate();
}
