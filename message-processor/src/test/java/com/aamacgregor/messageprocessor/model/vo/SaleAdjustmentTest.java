package com.aamacgregor.messageprocessor.model.vo;

import com.aamacgregor.messageprocessor.exception.MissingAdjustmentOperationException;
import com.aamacgregor.messageprocessor.exception.MissingAdjustmentValueException;
import com.aamacgregor.messageprocessor.exception.MissingProductException;
import com.aamacgregor.messageprocessor.model.enums.AdjustmentOperation;
import org.junit.Test;

import java.math.BigDecimal;

public class SaleAdjustmentTest {

    @Test(expected = MissingProductException.class)
    public void givenNoProductIsProvided_whenConstructorCalled_thenExpectException() {
        new SaleValueAdjustment("", BigDecimal.ONE, AdjustmentOperation.ADD);
    }

    @Test(expected = MissingAdjustmentValueException.class)
    public void givenNoProductValueIsProvided_whenConstructorCalled_thenExpectException() {
        new SaleValueAdjustment("Apple", null, AdjustmentOperation.ADD);
    }

    @Test(expected = MissingAdjustmentOperationException.class)
    public void givenNoQuantity_whenConstructorCalled_thenExpectAQuantityOfOne() {
        new SaleValueAdjustment("Apple", BigDecimal.ONE, null);
    }
}
