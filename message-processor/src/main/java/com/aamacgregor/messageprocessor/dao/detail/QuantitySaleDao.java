package com.aamacgregor.messageprocessor.dao.detail;

import com.aamacgregor.messageprocessor.dao.IQuantitySaleDao;
import com.aamacgregor.messageprocessor.exception.MissingProductException;
import com.aamacgregor.messageprocessor.exception.MissingSaleException;
import com.aamacgregor.messageprocessor.model.vo.QuantitySale;
import com.aamacgregor.messageprocessor.model.vo.SaleValueAdjustment;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.BiFunction;

public class QuantitySaleDao implements IQuantitySaleDao {

    private final BiFunction<String, Queue<QuantitySale>, Queue<QuantitySale>> adjustmentFunction = (key, value) -> value;

    //The String is the product name
    private Map<String, Queue<QuantitySale>> salesMap = new ConcurrentHashMap<>();

    @Override
    public void createSale(QuantitySale quantitySale) {
        String product = quantitySale.getSale()
                .orElseThrow(MissingSaleException::new)
                .getProduct()
                .orElseThrow(MissingProductException::new);

        salesMap.putIfAbsent(product, new ConcurrentLinkedDeque<>());
        salesMap.get(product).add(quantitySale);
    }

    @Override
    public void updateSalesValue(SaleValueAdjustment saleValueAdjustment) {
        String product = saleValueAdjustment
                .getProduct()
                .orElseThrow(MissingProductException::new);

        salesMap.computeIfPresent(product, adjustmentFunction);

    }


}
