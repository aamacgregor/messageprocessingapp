package com.aamacgregor.messageprocessor.service;

import com.aamacgregor.messageprocessor.model.vo.QuantitySale;
import com.aamacgregor.messageprocessor.model.vo.Sale;
import com.aamacgregor.messageprocessor.validator.IQuantitySaleValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalesService {

    private List<IQuantitySaleValidator> validators;

    public SalesService(@Autowired List<IQuantitySaleValidator> validators) {
        this.validators = validators;
    }

    public void processSale(Sale sale) {
        processSale(new QuantitySale(1, sale));
    }

    public void processSale(QuantitySale sale) {
        validators.forEach(iQuantitySaleValidator -> iQuantitySaleValidator.validate(sale));
    }
}
