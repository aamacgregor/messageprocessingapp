package com.aamacgregor.messageprocessor.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReportScheduler {

    //These two values would normally be configurable;
    private final int SALES_REPORT_INTERVAL = 10;
    private final int ADJUSTMENTS_REPORT_INTERVAL = 50;
    private int processCount = 0;


    private final ISalesReportGenerator salesReportGenerator;
    private final ISalesAdjustmentReportGenerator adjustmentReportGenerator;

    public ReportScheduler(@Autowired ISalesReportGenerator salesReportGenerator,
                           @Autowired ISalesAdjustmentReportGenerator adjustmentReportGenerator) {
        this.salesReportGenerator = salesReportGenerator;
        this.adjustmentReportGenerator = adjustmentReportGenerator;
    }

    public void process() {
        ++processCount;

        generateAtInterval(SALES_REPORT_INTERVAL, salesReportGenerator);
        generateAtInterval(ADJUSTMENTS_REPORT_INTERVAL, adjustmentReportGenerator);
    }

    private void generateAtInterval(int interval, IReportGenerator reportGenerator) {
        boolean salesReportRequired = processCount % interval == 0;
        if (salesReportRequired) {
            reportGenerator.generate();
        }
    }
}
