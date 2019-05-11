package com.aamacgregor.messageprocessor.validator;

import com.aamacgregor.messageprocessor.exception.InvalidProductValueException;
import com.aamacgregor.messageprocessor.exception.InvalidQuantityException;
import com.aamacgregor.messageprocessor.model.vo.Sale;
import org.springframework.stereotype.Component;

import static com.aamacgregor.messageprocessor.utils.ValidationUtils.validateBigDecimalIsGreaterThanZero;
import static com.aamacgregor.messageprocessor.utils.ValidationUtils.validateProduct;


@Component
public class SaleValidator {
    public void validate(Sale sale) {
        validateProduct(sale.getProduct());
        validateBigDecimalIsGreaterThanZero(sale.getValue(), InvalidProductValueException::new);
        validateQuantity(sale.getQuantity());
    }

    private void validateQuantity(int quantity) {
        //Check the quantity is greater than zero
        if (quantity <= 0) {
            throw new InvalidQuantityException();
        }
    }

}
