package com.aamacgregor.messageprocessor.model.vo;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Sales Summary data for use in the Sales Summary Report
 */
public class SalesSummary {
    private final String product;
    private final int quantity;
    private final BigDecimal totalValue;

    public SalesSummary(String product, int quantity, BigDecimal totalValue) {
        this.product = product;
        this.quantity = quantity;
        this.totalValue = totalValue;
    }

    public String getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SalesSummary that = (SalesSummary) o;
        return quantity == that.quantity &&
                Objects.equals(product, that.product) &&
                Objects.equals(totalValue, that.totalValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, quantity, totalValue);
    }

    @Override
    public String toString() {
        return "SalesSummary{" +
                "product='" + product + '\'' +
                ", quantity=" + quantity +
                ", totalValue=" + totalValue +
                '}';
    }
}
