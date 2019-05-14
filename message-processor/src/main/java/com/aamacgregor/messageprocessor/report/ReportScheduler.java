package com.aamacgregor.messageprocessor.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * Responsible for scheduling report generation
 */
@Component
public class ReportScheduler {

    private int messageCount = 0;
    private final List<IReportGenerator> reportSchedulerTasks;


    /**
     * Constructs a ReportScheduler
     *
     * @param reportSchedulerTasks the report generators to use when generating the reports
     */
    public ReportScheduler(@Autowired Collection<IReportGenerator> reportSchedulerTasks) {
        this.reportSchedulerTasks = new ArrayList<>(reportSchedulerTasks);
        this.reportSchedulerTasks.sort(Comparator.comparing(IReportGenerator::getScheduleInterval));
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
