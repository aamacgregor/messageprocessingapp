package com.aamacgregor.messageprocessor.model.vo;

import com.aamacgregor.messageprocessor.exception.MissingAdjustmentOperationException;
import com.aamacgregor.messageprocessor.exception.MissingAdjustmentValueException;
import com.aamacgregor.messageprocessor.exception.MissingProductException;
import com.aamacgregor.messageprocessor.model.enums.AdjustmentOperation;
import com.aamacgregor.messageprocessor.utils.StringUtils;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

/**
 * SaleValueAdjustment A.K.A MessageType3
 */
public class SaleValueAdjustment {
    private String product;
    private BigDecimal adjustment;
    private AdjustmentOperation adjustmentOperation;

    /**
     * Constructs a SaleValueAdjustment instance
     *
     * @param product             the product for which the adjustment should be made
     * @param adjustment          the adjustment that should be made to the value of each sale of the specified product
     * @param adjustmentOperation the adjustment type
     */
    public SaleValueAdjustment(String product, BigDecimal adjustment, AdjustmentOperation adjustmentOperation) {
        this.product = Optional.ofNullable(product).map(String::trim)
                .flatMap(StringUtils::emptyToNull)
                .orElseThrow(MissingProductException::new);
        this.adjustment = Optional.ofNullable(adjustment).orElseThrow(MissingAdjustmentValueException::new);
        this.adjustmentOperation = Optional.ofNullable(adjustmentOperation)
                .orElseThrow(MissingAdjustmentOperationException::new);
    }

    /**
     * @return The product name
     */
    public String getProduct() {
        return product;
    }

    /**
     * @return The adjustment to the value of the product
     */
    public BigDecimal getAdjustment() {
        return adjustment;
    }

    /**
     * @return The type of adjustment to be applied
     */
    public AdjustmentOperation getAdjustmentOperation() {
        return adjustmentOperation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SaleValueAdjustment that = (SaleValueAdjustment) o;
        return Objects.equals(product, that.product) &&
                Objects.equals(adjustment, that.adjustment) &&
                adjustmentOperation == that.adjustmentOperation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, adjustment, adjustmentOperation);
    }

    @Override
    public String toString() {
        return "SaleValueAdjustment{" +
                "product='" + product + '\'' +
                ", adjustment=" + adjustment +
                ", adjustmentOperation=" + adjustmentOperation +
                '}';
    }
}
