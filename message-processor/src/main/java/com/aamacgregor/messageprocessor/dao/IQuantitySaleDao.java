package com.aamacgregor.messageprocessor.dao;

import com.aamacgregor.messageprocessor.model.vo.QuantitySale;
import com.aamacgregor.messageprocessor.model.vo.SaleValueAdjustment;

public interface IQuantitySaleDao {

    void createSale(QuantitySale quantitySale);

    void updateSalesValue(SaleValueAdjustment saleValueAdjustment);

}
