package com.aamacgregor.messageprocessor.service;

import com.aamacgregor.messageprocessor.accessor.service.SalesAccessorService;
import com.aamacgregor.messageprocessor.accessor.service.SalesAdjustmentAccessorService;
import com.aamacgregor.messageprocessor.exception.ProcessOfSaleAdjustmentNotAllowedException;
import com.aamacgregor.messageprocessor.exception.ProcessOfSaleNotAllowedException;
import com.aamacgregor.messageprocessor.model.enums.AdjustmentOperation;
import com.aamacgregor.messageprocessor.model.vo.Sale;
import com.aamacgregor.messageprocessor.model.vo.SaleValueAdjustment;
import com.aamacgregor.messageprocessor.processor.IProcessingState;
import com.aamacgregor.messageprocessor.processor.ProcessManager;
import com.aamacgregor.messageprocessor.report.ReportScheduler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SalesServiceTest {
    private SalesAccessorService salesAccessorService;
    private SalesAdjustmentAccessorService salesAdjustmentAccessorService;
    private ReportScheduler reportScheduler;
    private ProcessManager processManager;

    private SalesService salesService;

    @Before
    public void setup() {
        salesAccessorService = mock(SalesAccessorService.class);
        salesAdjustmentAccessorService = mock(SalesAdjustmentAccessorService.class);
        reportScheduler = mock(ReportScheduler.class);
        processManager = mock(ProcessManager.class);


        salesService = new SalesService(salesAccessorService, salesAdjustmentAccessorService,
                reportScheduler, processManager);
    }

    @Test
    public void givenSalesMessage_whenProcessIsCalled_thenCheckTheMessageIsProcessed() {

        //Given
        Sale sale = new Sale("Apple", BigDecimal.valueOf(10), 1);
        Mockito.when(processManager.process()).thenReturn(true);

        //When
        salesService.processSale(sale);

        //Then
        Mockito.verify(processManager, times(1)).process();
        Mockito.verify(processManager, never()).setState(any(IProcessingState.class));
        Mockito.verify(salesAccessorService, times(1)).createSale(sale);
        Mockito.verify(salesAccessorService, never()).calculateSalesSummaries();
        Mockito.verify(salesAdjustmentAccessorService, never()).applySalesAdjustment(any(SaleValueAdjustment.class));
        Mockito.verify(salesAdjustmentAccessorService, never()).readSalesAdjustments();
        Mockito.verify(reportScheduler, times(1)).process();
    }

    @Test
    public void givenSalesMessageAndProcessLimitReached_whenProcessIsCalled_thenExpectProcessOfSaleNotAllowedException() {

        //Given
        Sale sale = new Sale("Apple", BigDecimal.valueOf(10), 1);
        Mockito.when(processManager.process()).thenReturn(false);

        try {
            //When
            salesService.processSale(sale);
            fail("Expected processSale to throw an exception");
        } catch (ProcessOfSaleNotAllowedException e) {
            //Then
            Mockito.verify(processManager, times(1)).process();
            Mockito.verify(processManager, never()).setState(any(IProcessingState.class));
            Mockito.verify(salesAccessorService, never()).createSale(any(Sale.class));
            Mockito.verify(salesAccessorService, never()).calculateSalesSummaries();
            Mockito.verify(salesAdjustmentAccessorService, never()).applySalesAdjustment(any(SaleValueAdjustment.class));
            Mockito.verify(salesAdjustmentAccessorService, never()).readSalesAdjustments();
            Mockito.verify(reportScheduler, never()).process();
        }
    }

    @Test
    public void givenSaleAdjustmentMessage_whenProcessIsCalled_thenCheckTheMessageIsProcessed() {

        //Given
        SaleValueAdjustment adjustment = new SaleValueAdjustment("Apple",
                BigDecimal.valueOf(10),
                AdjustmentOperation.ADD);
        Mockito.when(processManager.process()).thenReturn(true);

        //When
        salesService.processAdjustment(adjustment);

        //Then
        Mockito.verify(processManager, times(1)).process();
        Mockito.verify(processManager, never()).setState(any(IProcessingState.class));
        Mockito.verify(salesAccessorService, never()).createSale(any(Sale.class));
        Mockito.verify(salesAccessorService, never()).calculateSalesSummaries();
        Mockito.verify(salesAdjustmentAccessorService, times(1)).applySalesAdjustment(adjustment);
        Mockito.verify(salesAdjustmentAccessorService, never()).readSalesAdjustments();
        Mockito.verify(reportScheduler, times(1)).process();
    }

    @Test
    public void givenSaleAdjustmentMessageAndProcessLimitReached_whenProcessIsCalled_thenExpectProcessOfSaleAdjustmentNotAllowedException() {

        //Given
        SaleValueAdjustment adjustment = new SaleValueAdjustment("Apple",
                BigDecimal.valueOf(10),
                AdjustmentOperation.ADD);
        Mockito.when(processManager.process()).thenReturn(false);

        try {
            //When
            salesService.processAdjustment(adjustment);
            fail("Expected processAdjustment to throw an exception");
        } catch (ProcessOfSaleAdjustmentNotAllowedException e) {
            //Then
            Mockito.verify(processManager, times(1)).process();
            Mockito.verify(processManager, never()).setState(any(IProcessingState.class));
            Mockito.verify(salesAccessorService, never()).createSale(any(Sale.class));
            Mockito.verify(salesAccessorService, never()).calculateSalesSummaries();
            Mockito.verify(salesAdjustmentAccessorService, never()).applySalesAdjustment(any(SaleValueAdjustment.class));
            Mockito.verify(salesAdjustmentAccessorService, never()).readSalesAdjustments();
            Mockito.verify(reportScheduler, never()).process();
        }
    }
}
