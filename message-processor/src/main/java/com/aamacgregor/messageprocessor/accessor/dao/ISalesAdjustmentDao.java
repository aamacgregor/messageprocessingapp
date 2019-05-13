package com.aamacgregor.messageprocessor.accessor.dao;

import com.aamacgregor.messageprocessor.model.vo.SaleValueAdjustment;

import java.util.Collection;

/**
 * Responsible for creating sale adjustments and retrieving sale adjustments
 */
public interface ISalesAdjustmentDao {

    /**
     * Records the sale adjustment
     *
     * @param saleValueAdjustment the sale adjustment to record
     */
    void createSalesAdjustment(SaleValueAdjustment saleValueAdjustment);

    /**
     * @return a list of all of the sales adjustments
     */
    Collection<SaleValueAdjustment> readSalesAdjustments();
}
