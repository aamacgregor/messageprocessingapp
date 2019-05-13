package com.aamacgregor.messageprocessor.controller;

import com.aamacgregor.messageprocessor.model.vo.Sale;
import com.aamacgregor.messageprocessor.model.vo.SaleValueAdjustment;
import com.aamacgregor.messageprocessor.service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest Controller for handling sales messages
 */
@RestController
public class SalesController {

    private final SalesService salesService;

    public SalesController(@Autowired SalesService salesService) {
        this.salesService = salesService;
    }

    /**
     * processes a sale, handles both MessageType1 and MessageType2 messages.
     *
     * @param sale the sale to process
     */
    @PostMapping("/api/sale")
    public void processSale(@RequestBody Sale sale) {
        salesService.processSale(sale);
    }

    /**
     * processes sale adjustments
     *
     * @param saleValueAdjustment the sale adjustment to process
     */
    @PostMapping("/api/adjustment")
    public void processSaleValueAdjustment(@RequestBody SaleValueAdjustment saleValueAdjustment) {
        salesService.processAdjustment(saleValueAdjustment);
    }
}
