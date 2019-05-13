package com.aamacgregor.messageprocessor.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class ReportScheduler {

    private int messageCount = 0;
    private final Collection<ReportSchedulerTask> reportSchedulerTasks;


    public ReportScheduler(@Autowired Collection<ReportSchedulerTask> reportSchedulerTasks) {
        this.reportSchedulerTasks = reportSchedulerTasks;
    }

    public void process() {
        ++messageCount;
        reportSchedulerTasks.stream()
                .filter(task -> task.isScheduleInterval(messageCount))
                .forEach(ReportSchedulerTask::generate);
    }
}
