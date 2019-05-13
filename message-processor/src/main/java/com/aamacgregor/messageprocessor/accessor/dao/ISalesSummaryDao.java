package com.aamacgregor.messageprocessor.accessor.dao;

import com.aamacgregor.messageprocessor.model.vo.SalesSummary;

import java.util.Collection;

public interface ISalesSummaryDao {

    Collection<SalesSummary> readSalesSummaries();

}
