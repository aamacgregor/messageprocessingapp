package com.aamacgregor.messageprocessor.accessor.dao;

import com.aamacgregor.messageprocessor.model.vo.Sale;

import java.util.Collection;

/**
 * Responsible for creating, reading and updating sales
 */
public interface ISalesDao {

    /**
     * records a sale
     *
     * @param sale the sale to record
     */
    void createSale(Sale sale);

    /**
     * @return the list of sales recorded so far
     */
    Collection<Sale> readSales();


    /**
     * updates/replaces the existing sales with the provided sales
     *
     * @param sales the updated sales
     */
    void updateSales(Collection<Sale> sales);

}
