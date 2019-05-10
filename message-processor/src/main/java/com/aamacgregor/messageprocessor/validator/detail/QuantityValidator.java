package com.aamacgregor.messageprocessor.validator.detail;

import com.aamacgregor.messageprocessor.exception.InvalidQuantityException;
import com.aamacgregor.messageprocessor.exception.MissingQuantityException;
import com.aamacgregor.messageprocessor.model.vo.QuantitySale;
import com.aamacgregor.messageprocessor.validator.IQuantitySaleValidator;
import org.springframework.stereotype.Component;

@Component
public class QuantityValidator implements IQuantitySaleValidator {
    @Override
    public void validate(QuantitySale quantitySale) {
        //Check the Sale object exists
        int quantity = quantitySale.getQuantity().orElseThrow(MissingQuantityException::new);

        //Check the quantity is greater than zero
        if (quantity <= 0) {
            throw new InvalidQuantityException();
        }
    }
}
