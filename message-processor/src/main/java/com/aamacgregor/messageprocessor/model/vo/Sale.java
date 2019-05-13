package com.aamacgregor.messageprocessor.model.vo;

import com.aamacgregor.messageprocessor.exception.MissingProductException;
import com.aamacgregor.messageprocessor.exception.MissingProductValueException;
import com.aamacgregor.messageprocessor.utils.StringUtils;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

/**
 * The Sale class covers both MessageType1 and MessageType2
 *
 * If MessageType1 is received by the SalesController
 * {
 *     "product" : "Apple",
 *     "value" : 5
 * }
 *
 * then the Sale object will construct with a quantity of one.
 */
public class Sale {
    private static final int MINIMUM_QUANTITY = 1;

    private String product;
    private BigDecimal value;
    private int quantity;

    /**
     * Constructs a SaleValueAdjustment instance
     *
     * @param product the product for which the sale should be made
     * @param value   the value of the product
     */
    public Sale(String product, BigDecimal value, Integer quantity) {
        this.product = Optional.ofNullable(product).map(String::trim)
                .flatMap(StringUtils::emptyToNull)
                .orElseThrow(MissingProductException::new);
        this.value = Optional.ofNullable(value).orElseThrow(MissingProductValueException::new);
        this.quantity = Optional.ofNullable(quantity).orElse(MINIMUM_QUANTITY);
    }

    public String getProduct() {
        return product;
    }

    public BigDecimal getValue() {
        return value;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sale sale = (Sale) o;
        return quantity == sale.quantity &&
                Objects.equals(product, sale.product) &&
                Objects.equals(value, sale.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, value, quantity);
    }

    @Override
    public String toString() {
        return "Sale{" +
                "product='" + product + '\'' +
                ", value=" + value +
                ", quantity=" + quantity +
                '}';
    }
}
