package com.aamacgregor.messageprocessor.utils;

import com.aamacgregor.messageprocessor.exception.InvalidProductNameException;

import java.math.BigDecimal;
import java.util.function.Supplier;

public class ValidationUtils {

    public static void validateProduct(String product) {
        if (!product.matches("[A-Za-z0-9 ]+")) {
            throw new InvalidProductNameException();
        }
    }

    public static <T extends RuntimeException> void validateBigDecimalIsGreaterThanZero(BigDecimal value,
                                                                                        Supplier<T> exceptionSupplier) {
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw exceptionSupplier.get();
        }
    }
}
