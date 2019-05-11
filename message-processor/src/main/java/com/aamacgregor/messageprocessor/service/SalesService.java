package com.aamacgregor.messageprocessor.service;

import com.aamacgregor.messageprocessor.dao.ISalesAdjustmentDao;
import com.aamacgregor.messageprocessor.dao.ISalesDao;
import com.aamacgregor.messageprocessor.model.vo.Sale;
import com.aamacgregor.messageprocessor.model.vo.SaleValueAdjustment;
import com.aamacgregor.messageprocessor.report.ReportScheduler;
import com.aamacgregor.messageprocessor.validator.SaleAdjustmentValidator;
import com.aamacgregor.messageprocessor.validator.SaleValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SalesService {

    private final ISalesDao salesDao;
    private final ISalesAdjustmentDao salesAdjustmentDao;
    private final ReportScheduler reportManager;

    private final SaleValidator validator = new SaleValidator();
    private final SaleAdjustmentValidator adjustmentValidator = new SaleAdjustmentValidator();

    public SalesService(@Autowired ISalesDao salesDao,
                        @Autowired ISalesAdjustmentDao salesAdjustmentDao,
                        @Autowired ReportScheduler reportManager) {
        this.salesDao = salesDao;
        this.salesAdjustmentDao = salesAdjustmentDao;
        this.reportManager = reportManager;
    }

    public void processSale(Sale sale) {
        validator.validate(sale);
        salesDao.createSale(sale);
        reportManager.process();
    }

    public void processAdjustment(SaleValueAdjustment saleValueAdjustment) {
        adjustmentValidator.validate(saleValueAdjustment);
        salesAdjustmentDao.createSalesAdjustment(saleValueAdjustment);
        reportManager.process();
    }
}
