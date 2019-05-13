package com.aamacgregor.messageprocessor.report.detail;

import com.aamacgregor.messageprocessor.accessor.dao.ISalesSummaryDao;
import com.aamacgregor.messageprocessor.model.vo.SalesSummary;
import com.aamacgregor.messageprocessor.report.IReportConsumer;
import com.aamacgregor.messageprocessor.report.ISalesReportGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class SalesAsciiReportGenerator implements ISalesReportGenerator {

    private static final String TABLE_NAME = "Sales Report";
    private static final String PRODUCT_COLUMN_LABEL = "Product";
    private static final String QUANTITY_COLUMN_LABEL = "Quantity";
    private static final String TOTAL_VALUE_COLUMN_LABEL = "Total Value";

    private final ISalesSummaryDao salesSummaryDao;
    private final IReportConsumer reportConsumer;

    public SalesAsciiReportGenerator(@Autowired ISalesSummaryDao salesSummaryDao,
                                     @Autowired IReportConsumer reportConsumer) {
        this.salesSummaryDao = salesSummaryDao;
        this.reportConsumer = reportConsumer;
    }

    @Override
    public void generate() {
        ReportTableGenerator tableGenerator = new ReportTableGenerator(TABLE_NAME,
                PRODUCT_COLUMN_LABEL,
                QUANTITY_COLUMN_LABEL,
                TOTAL_VALUE_COLUMN_LABEL);

        Collection<SalesSummary> salesSummaries = salesSummaryDao.readSalesSummaries();
        salesSummaries.forEach(salesSummary ->
                tableGenerator.addRow(salesSummary.getProduct(),
                        salesSummary.getQuantity(),
                        salesSummary.getTotalValue()));

        reportConsumer.consume(tableGenerator.generate());
    }
}
