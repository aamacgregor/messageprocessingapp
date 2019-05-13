package com.aamacgregor.messageprocessor.accessor.dao;

import com.aamacgregor.messageprocessor.model.vo.SaleValueAdjustment;

import java.util.Collection;

public interface ISalesAdjustmentDao {

    void createSalesAdjustment(SaleValueAdjustment saleValueAdjustment);

    Collection<SaleValueAdjustment> readSalesAdjustments();
}
