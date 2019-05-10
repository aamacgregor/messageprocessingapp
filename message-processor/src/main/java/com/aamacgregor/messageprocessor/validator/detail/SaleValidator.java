package com.aamacgregor.messageprocessor.validator.detail;

import com.aamacgregor.messageprocessor.exception.InvalidProductValueException;
import com.aamacgregor.messageprocessor.exception.MissingProductException;
import com.aamacgregor.messageprocessor.exception.MissingProductValueException;
import com.aamacgregor.messageprocessor.exception.MissingSaleException;
import com.aamacgregor.messageprocessor.model.vo.QuantitySale;
import com.aamacgregor.messageprocessor.model.vo.Sale;
import com.aamacgregor.messageprocessor.utils.StringUtils;
import com.aamacgregor.messageprocessor.validator.IQuantitySaleValidator;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


@Component
public class SaleValidator implements IQuantitySaleValidator {
    @Override
    public void validate(QuantitySale quantitySale) {
        //Check the Sale object exists
        Sale sale = quantitySale.getSale().orElseThrow(MissingSaleException::new);

        //Check that the product field exists and has a non-empty value
        sale.getProduct()
                .map(String::trim)
                .flatMap(StringUtils::emptyToNull)
                .orElseThrow(MissingProductException::new);

        //Check that the value field exists
        BigDecimal value = sale.getValue().orElseThrow(MissingProductValueException::new);

        //Check the value is not less than zero
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidProductValueException();
        }
    }
}
