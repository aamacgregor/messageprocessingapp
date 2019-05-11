package com.aamacgregor.messageprocessor.validator;

import com.aamacgregor.messageprocessor.exception.InvalidAdjustmentValueException;
import com.aamacgregor.messageprocessor.model.vo.SaleValueAdjustment;

import static com.aamacgregor.messageprocessor.utils.ValidationUtils.validateBigDecimalIsGreaterThanZero;
import static com.aamacgregor.messageprocessor.utils.ValidationUtils.validateProduct;

public class SaleAdjustmentValidator {

    public void validate(SaleValueAdjustment saleValueAdjustment) {
        validateProduct(saleValueAdjustment.getProduct());
        validateBigDecimalIsGreaterThanZero(saleValueAdjustment.getAdjustment(), InvalidAdjustmentValueException::new);
    }
}
