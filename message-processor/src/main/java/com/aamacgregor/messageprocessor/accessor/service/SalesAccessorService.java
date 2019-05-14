package com.aamacgregor.messageprocessor.accessor.service;

import com.aamacgregor.messageprocessor.accessor.dao.ISalesDao;
import com.aamacgregor.messageprocessor.model.vo.Sale;
import com.aamacgregor.messageprocessor.model.vo.SalesSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Responsible for passing sales to the sales DAO and calculating the sales summary
 */
@Component
public class SalesAccessorService {

    private ISalesDao salesDao;

    public SalesAccessorService(@Autowired ISalesDao salesDao) {
        this.salesDao = salesDao;
    }

    /**
     * Calculate and return the sales summary
     *
     * @return the calculated sales summary
     */
    public Collection<SalesSummary> calculateSalesSummaries() {

        //Collect sales by product
        Map<String, List<Sale>> productSales = salesDao.readSales().stream()
                .collect(Collectors.groupingBy(Sale::getProduct));

        //Convert each map entry (a collection of sales for a particular product) to a SalesSummary
        return productSales.entrySet().stream()
                .map(entry -> calculateSaleSummary(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(SalesSummary::getProduct))
                .collect(Collectors.toList());
    }

    private SalesSummary calculateSaleSummary(String product, Collection<Sale> sales) {
        int quantity = sales.stream()
                .map(Sale::getQuantity)
                .mapToInt(Integer::intValue)
                .sum();
        BigDecimal totalValue = sales.stream()
                .map(sale -> sale.getValue().multiply(BigDecimal.valueOf(sale.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new SalesSummary(product, quantity, totalValue);
    }

    /**
     * Records a sale entry
     *
     * @param sale the sale to record
     */
    public void createSale(Sale sale) {
        salesDao.createSale(sale);
    }
}
