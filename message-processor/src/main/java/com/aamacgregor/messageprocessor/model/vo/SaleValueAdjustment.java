package com.aamacgregor.messageprocessor.model.vo;

import com.aamacgregor.messageprocessor.model.enums.AdjustmentOperation;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * SaleValueAdjustment A.K.A MessageType3
 *
 * The rational for composition of product, adjustment and adjustmentOperation variables rather than
 * Sale and AdjustmentOperation variables is for clarity. A Sale object had a value which is semantically not
 * the same as an adjustment.
 */
public class SaleValueAdjustment {
    private String product;
    private BigDecimal adjustment;
    private AdjustmentOperation adjustmentOperation;

    /**
     * Constructs a SaleValueAdjustment instance
     * @param product the product for which the adjustment should be made
     * @param adjustment the adjustment that should be made to the value of each sale of the specified product
     * @param adjustmentOperation the adjustment type
     */
    public SaleValueAdjustment(String product, BigDecimal adjustment, AdjustmentOperation adjustmentOperation) {
        this.product = product;
        this.adjustment = adjustment;
        this.adjustmentOperation = adjustmentOperation;
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
     * @return Optional.empty() if adjustment is null, else an Optional containing a BigDecimal instance
     */
    public Optional<BigDecimal> getAdjustment() {
        return Optional.ofNullable(adjustment);
    }

    /**
     * Returns an AdjustmentOperation Optional
     * @return Optional.empty() if adjustmentOperation is null, else an Optional containing an AdjustmentOperation instance
     */
    public Optional<AdjustmentOperation> getAdjustmentOperation() {
        return Optional.ofNullable(adjustmentOperation);
    }
}
