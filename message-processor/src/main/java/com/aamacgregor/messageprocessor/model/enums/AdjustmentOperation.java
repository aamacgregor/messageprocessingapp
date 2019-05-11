package com.aamacgregor.messageprocessor.model.enums;

import java.math.BigDecimal;
import java.util.function.BiFunction;

public enum AdjustmentOperation {
    ADD(BigDecimal::add),
    SUBTRACT(BigDecimal::subtract),
    MULTIPLY(BigDecimal::multiply);


    /**
     * @param a The left hand value of the operation
     * @param b The right hand value of the operation
     * @return The result of applying the operation if the result is not less than zero, else BigDecimal.ZERO.
     */
    public BigDecimal apply(BigDecimal a, BigDecimal b) {
        BigDecimal result = operation.apply(a, b);
        return result.compareTo(BigDecimal.ZERO) < 0
                ? BigDecimal.ZERO
                : result;
    }

    /**
     * @return The name of the enum with the first letter uppercase and the rest lowercase
     */
    @Override
    public String toString() {
        String name = name();
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    AdjustmentOperation(BiFunction<BigDecimal, BigDecimal, BigDecimal> operation) {
        this.operation = operation;
    }

    private final BiFunction<BigDecimal, BigDecimal, BigDecimal> operation;
}
