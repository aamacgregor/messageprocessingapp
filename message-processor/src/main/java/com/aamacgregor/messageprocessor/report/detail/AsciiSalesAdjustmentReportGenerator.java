package com.aamacgregor.messageprocessor.report.detail;

import com.aamacgregor.messageprocessor.accessor.service.SalesAdjustmentAccessorService;
import com.aamacgregor.messageprocessor.model.vo.SaleValueAdjustment;
import com.aamacgregor.messageprocessor.report.IReportConsumer;
import com.aamacgregor.messageprocessor.report.IReportGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Responsible for generating a sales adjustment report using ascii characters.
 */
@Component
public class AsciiSalesAdjustmentReportGenerator implements IReportGenerator {

    private static final String TABLE_NAME = "Sales Adjustment Report";
    private static final String PRODUCT_COLUMN_LABEL = "Product";
    private static final String ADJUSTMENT_COLUMN_LABEL = "Adjustment";
    private static final String ADJUSTMENT_OPERATION_COLUMN_LABEL = "Adjustment Operation";

    private final SalesAdjustmentAccessorService salesAdjustmentAccessorService;
    private final IReportConsumer reportConsumer;

    @Value("${adjustment_report_interval}")
    private int scheduleInterval;

    /**
     * @param salesAdjustmentAccessorService the service to use to retrieve sales adjustments
     * @param reportConsumer                 the consumer to which the report table should be supplied.
     */
    public AsciiSalesAdjustmentReportGenerator(@Autowired SalesAdjustmentAccessorService salesAdjustmentAccessorService,
                                               @Autowired IReportConsumer reportConsumer) {
        this.salesAdjustmentAccessorService = salesAdjustmentAccessorService;
        this.reportConsumer = reportConsumer;
    }


    @Override
    public int getScheduleInterval() {
        return scheduleInterval;
    }

    @Override
    public void generate() {
        ReportTableGenerator tableGenerator = new ReportTableGenerator(TABLE_NAME,
                PRODUCT_COLUMN_LABEL,
                ADJUSTMENT_COLUMN_LABEL,
                ADJUSTMENT_OPERATION_COLUMN_LABEL);

        Collection<SaleValueAdjustment> adjustments = salesAdjustmentAccessorService.readSalesAdjustments();

        adjustments.forEach(salesSummary ->
                tableGenerator.addRow(salesSummary.getProduct(),
                        salesSummary.getAdjustment(),
                        salesSummary.getAdjustmentOperation()));

        reportConsumer.consume(tableGenerator.generate());
    }
}
