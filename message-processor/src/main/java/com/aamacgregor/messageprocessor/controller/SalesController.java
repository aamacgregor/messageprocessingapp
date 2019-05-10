package com.aamacgregor.messageprocessor.controller;

import com.aamacgregor.messageprocessor.model.vo.QuantitySale;
import com.aamacgregor.messageprocessor.model.vo.Sale;
import com.aamacgregor.messageprocessor.service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SalesController {

    private final SalesService salesService;

    public SalesController(@Autowired SalesService salesService) {
        this.salesService = salesService;
    }

    @PostMapping("/api/sale")
    public void processSale(@RequestBody Sale sale) {
        salesService.processSale(sale);
    }

    @PostMapping("/api/quantity/sale")
    public void processQuantitySale(@RequestBody QuantitySale quantitySale) {
        salesService.processSale(quantitySale);
    }
}
