package com.aamacgregor.messageprocessor.report.detail;

import com.aamacgregor.messageprocessor.report.IReportConsumer;
import org.springframework.stereotype.Component;

/**
 * Responsible for printing to the console
 */
@Component
public class ConsoleReportConsumer implements IReportConsumer {

    @Override
    public void consume(String report) {
        System.out.println("\n\n" + report);
    }
}
