package com.aamacgregor.messageprocessor.report;

public class ReportSchedulerTask {

    private final int scheduleInterval;
    private final IReportGenerator reportGenerator;

    public ReportSchedulerTask(int scheduleInterval, IReportGenerator reportGenerator) {
        this.scheduleInterval = scheduleInterval;
        this.reportGenerator = reportGenerator;
    }

    public boolean isScheduleInterval(int messageCount) {
        return messageCount % scheduleInterval == 0;
    }

    public void generate() {
        reportGenerator.generate();
    }
}
