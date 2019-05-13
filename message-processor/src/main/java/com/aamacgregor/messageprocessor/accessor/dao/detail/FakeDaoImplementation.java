package com.aamacgregor.messageprocessor.accessor.dao.detail;

import com.aamacgregor.messageprocessor.accessor.dao.ISalesAdjustmentDao;
import com.aamacgregor.messageprocessor.accessor.dao.ISalesDao;
import com.aamacgregor.messageprocessor.accessor.dao.ISalesSummaryDao;
import com.aamacgregor.messageprocessor.model.vo.Sale;
import com.aamacgregor.messageprocessor.model.vo.SaleValueAdjustment;
import com.aamacgregor.messageprocessor.model.vo.SalesSummary;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class FakeDaoImplementation implements ISalesDao, ISalesSummaryDao, ISalesAdjustmentDao {

    private static final Function<Map.Entry<String, Queue<Sale>>, SalesSummary> SALES_SUMMARY_FUNCTION = entry -> {
        String product = entry.getKey();
        int quantity = entry.getValue().stream()
                .map(Sale::getQuantity)
                .mapToInt(Integer::intValue)
                .sum();
        BigDecimal totalValue = entry.getValue().stream()
                .map(sale -> sale.getValue().multiply(BigDecimal.valueOf(sale.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new SalesSummary(product, quantity, totalValue);
    };

    //The String is the product name
    private Map<String, Queue<Sale>> salesMap = new ConcurrentHashMap<>();
    private Queue<SaleValueAdjustment> salesAdjustments = new ConcurrentLinkedDeque<>();

    @Override
    public void createSale(Sale sale) {
        String product = sale.getProduct();
        salesMap.putIfAbsent(product, new ConcurrentLinkedDeque<>());
        salesMap.get(product).add(sale);
    }

    @Override
    public void createSalesAdjustment(SaleValueAdjustment saleValueAdjustment) {
        salesAdjustments.add(saleValueAdjustment);
        salesMap.computeIfPresent(saleValueAdjustment.getProduct(), new SalesValueAdjuster(saleValueAdjustment));
    }

    @Override
    public Collection<SaleValueAdjustment> readSalesAdjustments() {
        return new ArrayList<>(salesAdjustments);
    }

    @Override
    public Collection<Sale> readSales() {
        return salesMap.values().stream().flatMap(Queue::stream).collect(Collectors.toList());
    }

    @Override
    public Collection<SalesSummary> readSalesSummaries() {
        return salesMap.entrySet().stream().map(SALES_SUMMARY_FUNCTION).collect(Collectors.toList());
    }

    private class SalesValueAdjuster implements BiFunction<String, Queue<Sale>, Queue<Sale>> {

        private final SaleValueAdjustment saleValueAdjustment;

        public SalesValueAdjuster(SaleValueAdjustment saleValueAdjustment) {
            this.saleValueAdjustment = saleValueAdjustment;
        }

        @Override
        public Queue<Sale> apply(String s, Queue<Sale> sales) {
            return sales.stream()
                    .map(sale -> getSaleWithAdjustedValue(sale, saleValueAdjustment))
                    .collect(Collectors.toCollection(ConcurrentLinkedDeque::new));

        }

        private Sale getSaleWithAdjustedValue(Sale sale, SaleValueAdjustment adjustment) {
            return new Sale(sale.getProduct(),
                    adjustment.getAdjustmentOperation()
                            .apply(sale.getValue(), adjustment.getAdjustment()),
                    sale.getQuantity());
        }
    }
}
