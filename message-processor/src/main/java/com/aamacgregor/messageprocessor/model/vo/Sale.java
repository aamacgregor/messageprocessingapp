package com.aamacgregor.messageprocessor.model.vo;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Sale A.K.A MessageType1
 * 
 * Note that I've gone for immutability for the three value object types (Sale, QuantitySale and SaleValueAdjustment).
 */
public class Sale {
    private String product;
    private BigDecimal value;

    /**
     * Constructs a SaleValueAdjustment instance
     * @param product the product for which the sale should be made
     * @param value the value of the product
     */
    public Sale(String product, BigDecimal value) {
        this.product = product;
        this.value = value;
    }

    /**
     * Returns a String Optional
     * @return Optional.empty() if product is null, else an Optional containing a String instance
     */
    public Optional<String> getProduct() {
        return Optional.ofNullable(product);
    }

    /**
     * Returns a BigDecimal Optional
     * @return Optional.empty() if value is null, else an Optional containing a BigDecimal instance
     */
    public Optional<BigDecimal> getValue() {
        return Optional.ofNullable(value);
    }
}
