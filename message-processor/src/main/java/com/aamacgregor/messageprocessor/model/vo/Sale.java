package com.aamacgregor.messageprocessor.model.vo;

import com.aamacgregor.messageprocessor.exception.MissingProductException;
import com.aamacgregor.messageprocessor.exception.MissingProductValueException;
import com.aamacgregor.messageprocessor.utils.StringUtils;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

/**
 * The Sale class covers both MessageType1 and MessageType2
 * <p>
 * Rational for composition vs inheritance:
 * It could be argued that QuantitySale is a large sale and therefore should inherit from Sale (a sale);
 * however, from a behavioural point of view this would be a poor design. It does not make sense to pass an instance
 * of type QuantitySale as an argument to a process method that has a parameter of type Sale. i.e.
 * <p>
 * Let's say a concrete processor inherits from the following interface:
 * <p>
 * interface Processor {
 * boolean process(Sale message);
 * }
 * <p>
 * If we then do the following:
 * <p>
 * Processor processor = ...;
 * QuantitySale message = ...;
 * <p>
 * processor.process(message);
 * <p>
 * Then the QuantitySale instance would be incorrectly processed as only a single sale would be processed, not the
 * quantity of sales specified within the QuantitySale instance. Therefore since a QuantitySale instance cannot be
 * correctly processed using the Sale API, it turns out that QuantitySale is not a Sale and therefore
 * QuantitySale should not extend Sale. Instead, QuantitySale should comprise Sale and a quantity,
 * hence the use of composition.
 * <p>
 * That said, It could legitimately be argued that a Sale is a QuantitySale with a quantity of 1, making Sale
 * completely redundant, but since the problem specifies that messages can be received via one of three specific
 * message types, I'll just roll with it and keep Sale as a specified type.
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
