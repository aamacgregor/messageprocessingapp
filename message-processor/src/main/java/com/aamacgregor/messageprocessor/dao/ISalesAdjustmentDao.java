package com.aamacgregor.messageprocessor.dao;

import com.aamacgregor.messageprocessor.model.vo.SaleValueAdjustment;

import java.util.Collection;

public interface ISalesAdjustmentDao {

    void createSalesAdjustment(SaleValueAdjustment saleValueAdjustment);

    Collection<SaleValueAdjustment> readSalesAdjustments();
}
