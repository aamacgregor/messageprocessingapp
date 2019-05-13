package com.aamacgregor.messageprocessor.report.detail;

import com.aamacgregor.messageprocessor.accessor.dao.ISalesAdjustmentDao;
import com.aamacgregor.messageprocessor.model.vo.SaleValueAdjustment;
import com.aamacgregor.messageprocessor.report.IReportConsumer;
import com.aamacgregor.messageprocessor.report.ISalesAdjustmentReportGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class SalesAdjustmentAsciiReportGenerator implements ISalesAdjustmentReportGenerator {

    private static final String TABLE_NAME = "Sales Adjustment Report";
    private static final String PRODUCT_COLUMN_LABEL = "Product";
    private static final String ADJUSTMENT_COLUMN_LABEL = "Adjustment";
    private static final String ADJUSTMENT_OPERATION_COLUMN_LABEL = "Adjustment Operation";

    private final ISalesAdjustmentDao salesAdjustmentDao;
    private final IReportConsumer reportConsumer;

    public SalesAdjustmentAsciiReportGenerator(@Autowired ISalesAdjustmentDao salesAdjustmentDao,
                                               @Autowired IReportConsumer reportConsumer) {
        this.salesAdjustmentDao = salesAdjustmentDao;
        this.reportConsumer = reportConsumer;
    }

    @Override
    public void generate() {
        ReportTableGenerator tableGenerator = new ReportTableGenerator(TABLE_NAME,
                PRODUCT_COLUMN_LABEL,
                ADJUSTMENT_COLUMN_LABEL,
                ADJUSTMENT_OPERATION_COLUMN_LABEL);

        Collection<SaleValueAdjustment> adjustments = salesAdjustmentDao.readSalesAdjustments();

        adjustments.forEach(salesSummary ->
                tableGenerator.addRow(salesSummary.getProduct(),
                        salesSummary.getAdjustment(),
                        salesSummary.getAdjustmentOperation()));

        reportConsumer.consume(tableGenerator.generate());
    }
}
