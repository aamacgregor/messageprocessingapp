package com.aamacgregor.messageprocessor.model.vo;

import com.aamacgregor.messageprocessor.exception.MissingProductException;
import com.aamacgregor.messageprocessor.exception.MissingProductValueException;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class SaleTest {

    @Test(expected = MissingProductException.class)
    public void givenNoProductIsProvided_whenConstructorCalled_thenExpectException() {
        new Sale("", BigDecimal.ONE, 5);
    }

    @Test(expected = MissingProductValueException.class)
    public void givenNoProductValueIsProvided_whenConstructorCalled_thenExpectException() {
        new Sale("Apple", null, 5);
    }

    @Test
    public void givenNoQuantity_whenConstructorCalled_thenExpectAQuantityOfOne() {
        assertEquals(1, new Sale("Apple", BigDecimal.ONE, null).getQuantity());
    }
}
