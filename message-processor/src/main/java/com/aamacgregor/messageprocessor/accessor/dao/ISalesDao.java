package com.aamacgregor.messageprocessor.accessor.dao;

import com.aamacgregor.messageprocessor.model.vo.Sale;

import java.util.Collection;

public interface ISalesDao {

    void createSale(Sale sale);

    Collection<Sale> readSales();

}
