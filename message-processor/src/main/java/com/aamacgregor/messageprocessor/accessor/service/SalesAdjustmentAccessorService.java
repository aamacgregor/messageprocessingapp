package com.aamacgregor.messageprocessor.accessor.service;

import com.aamacgregor.messageprocessor.accessor.dao.ISalesAdjustmentDao;
import com.aamacgregor.messageprocessor.accessor.dao.ISalesDao;
import com.aamacgregor.messageprocessor.model.vo.Sale;
import com.aamacgregor.messageprocessor.model.vo.SaleValueAdjustment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Responsible for recording and applying sale adjustments.
 */
@Component
public class SalesAdjustmentAccessorService {

    private ISalesDao salesDao;
    private ISalesAdjustmentDao salesAdjustmentDao;

    public SalesAdjustmentAccessorService(@Autowired ISalesDao salesDao, @Autowired ISalesAdjustmentDao salesAdjustmentDao) {
        this.salesDao = salesDao;
        this.salesAdjustmentDao = salesAdjustmentDao;
    }

    /**
     * Applies the sales adjustment to all sales of the product specified by the adjustment. Records the adjustment
     *
     * @param saleValueAdjustment the adjustment to apply
     */
    public void applySalesAdjustment(SaleValueAdjustment saleValueAdjustment) {
        Collection<Sale> sales = salesDao.readSales();

        //Apply adjustments only to sales of the specified product
        Collection<Sale> adjustedSales = sales.stream()
                .filter(sale -> sale.getProduct().equals(saleValueAdjustment.getProduct()))
                .map(sale -> getSaleWithAdjustedValue(sale, saleValueAdjustment))
                .collect(Collectors.toList());


        //Now add back all the sales that are not of the specified product
        Collection<Sale> updatedSales = Stream.of(adjustedSales, sales.stream()
                .filter(sale -> !sale.getProduct().equals(saleValueAdjustment.getProduct()))
                .collect(Collectors.toList()))
                .flatMap(Collection::stream).collect(Collectors.toList());

        salesDao.updateSales(updatedSales);
        salesAdjustmentDao.createSalesAdjustment(saleValueAdjustment);
    }

    /**
     * Gets the list of sales adjustments made so far
     *
     * @return The list of adjustments
     */
    public Collection<SaleValueAdjustment> readSalesAdjustments() {
        return salesAdjustmentDao.readSalesAdjustments()
                .stream()
                .sorted(Comparator.comparing(SaleValueAdjustment::getProduct))
                .collect(Collectors.toList());
    }

    private Sale getSaleWithAdjustedValue(Sale sale, SaleValueAdjustment adjustment) {
        return new Sale(sale.getProduct(),
                adjustment.getAdjustmentOperation()
                        .apply(sale.getValue(), adjustment.getAdjustment()),
                sale.getQuantity());
    }
}
