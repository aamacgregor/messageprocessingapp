package com.aamacgregor.messageprocessor.report.detail;

import com.aamacgregor.messageprocessor.accessor.service.SalesAccessorService;
import com.aamacgregor.messageprocessor.model.vo.SalesSummary;
import com.aamacgregor.messageprocessor.report.IReportConsumer;
import com.aamacgregor.messageprocessor.report.IReportGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class AsciiSalesReportGenerator implements IReportGenerator {

    private static final String TABLE_NAME = "Sales Report";
    private static final String PRODUCT_COLUMN_LABEL = "Product";
    private static final String QUANTITY_COLUMN_LABEL = "Quantity";
    private static final String TOTAL_VALUE_COLUMN_LABEL = "Total Value";

    private final SalesAccessorService salesAccessorService;
    private final IReportConsumer reportConsumer;

    @Value("${sales_report_interval}")
    private int scheduleInterval;

    public AsciiSalesReportGenerator(@Autowired SalesAccessorService salesAccessorService,
                                     @Autowired IReportConsumer reportConsumer) {
        this.salesAccessorService = salesAccessorService;
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
                QUANTITY_COLUMN_LABEL,
                TOTAL_VALUE_COLUMN_LABEL);

        List<SalesSummary> salesSummaries = new ArrayList<>(salesAccessorService.calculateSalesSummaries());
        salesSummaries.sort(Comparator.comparing(SalesSummary::getProduct));

        salesSummaries.forEach(salesSummary ->
                tableGenerator.addRow(salesSummary.getProduct(),
                        salesSummary.getQuantity(),
                        salesSummary.getTotalValue()));

        reportConsumer.consume(tableGenerator.generate());
    }
}
