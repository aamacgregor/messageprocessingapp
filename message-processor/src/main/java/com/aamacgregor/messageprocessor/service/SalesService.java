package com.aamacgregor.messageprocessor.service;

import com.aamacgregor.messageprocessor.accessor.service.SalesAccessorService;
import com.aamacgregor.messageprocessor.accessor.service.SalesAdjustmentAccessorService;
import com.aamacgregor.messageprocessor.exception.ProcessOfSaleAdjustmentNotAllowedException;
import com.aamacgregor.messageprocessor.exception.ProcessOfSaleNotAllowedException;
import com.aamacgregor.messageprocessor.model.vo.Sale;
import com.aamacgregor.messageprocessor.model.vo.SaleValueAdjustment;
import com.aamacgregor.messageprocessor.processor.ProcessManager;
import com.aamacgregor.messageprocessor.report.ReportScheduler;
import com.aamacgregor.messageprocessor.validator.SaleAdjustmentValidator;
import com.aamacgregor.messageprocessor.validator.SaleValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Responsible for the calling the appropriate business logic to process the messages coming
 * from the SalesController.
 */
@Service
public class SalesService {

    private final SalesAccessorService salesAccessorService;
    private final SalesAdjustmentAccessorService salesAdjustmentAccessorService;
    private final ReportScheduler reportScheduler;
    private final ProcessManager processManager;

    private final SaleValidator validator = new SaleValidator();
    private final SaleAdjustmentValidator adjustmentValidator = new SaleAdjustmentValidator();

    /**
     * Constructs a SalesService
     *
     * @param salesAccessorService           the sales accessor to use when recording sales
     * @param salesAdjustmentAccessorService the sales adjustment accessor to use when recording asjustments
     * @param reportScheduler                the report scheduler to use for generating reports
     * @param processManager                 the process manager to use of knowing when messages can be processed
     */
    public SalesService(@Autowired SalesAccessorService salesAccessorService,
                        @Autowired SalesAdjustmentAccessorService salesAdjustmentAccessorService,
                        @Autowired ReportScheduler reportScheduler,
                        @Autowired ProcessManager processManager) {
        this.salesAccessorService = salesAccessorService;
        this.salesAdjustmentAccessorService = salesAdjustmentAccessorService;
        this.reportScheduler = reportScheduler;
        this.processManager = processManager;
    }

    /**
     * Processes a sale, this includes sale validation, sale recording and report generation
     *
     * @param sale the sale to process
     */
    public void processSale(Sale sale) {
        validator.validate(sale);
        if (processManager.process()) {
            salesAccessorService.createSale(sale);
            reportScheduler.process();
        } else {
            throw new ProcessOfSaleNotAllowedException();
        }
    }

    /**
     * Processes a sale, this includes sale adjustment validation, sale adjustment recording and
     * report generation
     *
     * @param saleValueAdjustment the sale adjustment to process
     */
    public void processAdjustment(SaleValueAdjustment saleValueAdjustment) {
        adjustmentValidator.validate(saleValueAdjustment);
        if (processManager.process()) {
            salesAdjustmentAccessorService.applySalesAdjustment(saleValueAdjustment);
            reportScheduler.process();
        } else {
            throw new ProcessOfSaleAdjustmentNotAllowedException();
        }
    }
}
