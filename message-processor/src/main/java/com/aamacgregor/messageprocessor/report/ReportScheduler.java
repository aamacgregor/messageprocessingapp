package com.aamacgregor.messageprocessor.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Responsible for scheduling report generation
 */
@Component
public class ReportScheduler {

    private int messageCount = 0;
    private final Collection<IReportGenerator> reportSchedulerTasks;


    /**
     * Constructs a ReportScheduler
     *
     * @param reportSchedulerTasks the report generators to use when generating the reports
     */
    public ReportScheduler(@Autowired Collection<IReportGenerator> reportSchedulerTasks) {
        this.reportSchedulerTasks = reportSchedulerTasks;
    }

    /**
     * Increments the an internal messageCount variable. Iterates over the report generators and
     * calls generate if the current message count modulo the report interval is zero.
     */
    public void process() {
        ++messageCount;
        reportSchedulerTasks.stream()
                .filter(this::isScheduleInterval)
                .forEach(IReportGenerator::generate);
    }

    private boolean isScheduleInterval(IReportGenerator generator) {
        return messageCount % generator.getScheduleInterval() == 0;
    }
}
