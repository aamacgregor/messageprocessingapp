package com.aamacgregor.messageprocessor.controller;

import com.aamacgregor.messageprocessor.accessor.dao.ISalesAdjustmentDao;
import com.aamacgregor.messageprocessor.accessor.dao.ISalesDao;
import com.aamacgregor.messageprocessor.accessor.dao.ISalesSummaryDao;
import com.aamacgregor.messageprocessor.model.vo.Sale;
import com.aamacgregor.messageprocessor.model.vo.SaleValueAdjustment;
import com.aamacgregor.messageprocessor.model.vo.SalesSummary;
import com.aamacgregor.messageprocessor.report.IReportConsumer;
import com.aamacgregor.messageprocessor.report.ReportScheduler;
import com.aamacgregor.messageprocessor.report.detail.SalesAdjustmentAsciiReportGenerator;
import com.aamacgregor.messageprocessor.report.detail.SalesAsciiReportGenerator;
import com.aamacgregor.messageprocessor.service.SalesService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest({SalesController.class, SalesService.class, ReportScheduler.class,
        SalesAdjustmentAsciiReportGenerator.class, SalesAsciiReportGenerator.class})
public class SalesControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private IReportConsumer reportConsumer;

    @MockBean
    private ISalesDao salesDao;

    @MockBean
    private ISalesAdjustmentDao salesAdjustmentDao;

    @MockBean
    ISalesSummaryDao salesSummaryDao;

    @Test
    public void givenOneProduct_whenOneValidSalesMessage_thenCheckItProcessesSuccessfully() throws Exception {
        mvc.perform(post("/api/sale")
                .content("{ \"product\" : \"Apple\"," +
                        "  \"value\" : \"20\"" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(reportConsumer, Mockito.never()).consume("");
        Mockito.verify(salesDao, Mockito.times(1))
                .createSale(new Sale("Apple", BigDecimal.valueOf(20), 1));
        Mockito.verify(salesDao, Mockito.never()).readSales();
        Mockito.verify(salesAdjustmentDao, Mockito.never()).createSalesAdjustment(any(SaleValueAdjustment.class));
        Mockito.verify(salesAdjustmentDao, Mockito.never()).readSalesAdjustments();
        Mockito.verify(salesSummaryDao, Mockito.never()).readSalesSummaries();
    }

    @Test
    public void givenTwoDifferentProducts_whenTenValidSalesMessages_thenCheckSalesReportIsSuccessfullyGenerated() throws Exception {

        Mockito.when(salesSummaryDao.readSalesSummaries()).thenReturn(Arrays.asList(
                new SalesSummary("Apple", 5, BigDecimal.valueOf(100)),
                new SalesSummary("Orange", 5, BigDecimal.valueOf(250))
        ));

        for (int i = 0; i < 5; i++) {
            mvc.perform(post("/api/sale")
                    .content("{ \"product\" : \"Apple\"," +
                            "  \"value\" : \"20\"" +
                            "}")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
            mvc.perform(post("/api/sale")
                    .content("{ \"product\" : \"Orange\"," +
                            "  \"value\" : \"10\"," +
                            "  \"quantity\" : \"5\"" +
                            "}")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        Mockito.verify(reportConsumer, Mockito.times(1)).consume("Sales Report\n" +
                "------------------------------\n" +
                "|Product|Quantity|Total Value|\n" +
                "------------------------------\n" +
                "|Apple  |5       |100        |\n" +
                "|Orange |5       |250        |\n" +
                "------------------------------\n");
        Mockito.verify(salesDao, Mockito.times(5))
                .createSale(new Sale("Apple", BigDecimal.valueOf(20), 1));
        Mockito.verify(salesDao, Mockito.times(5))
                .createSale(new Sale("Orange", BigDecimal.valueOf(10), 5));
        Mockito.verify(salesDao, Mockito.never()).readSales();
        Mockito.verify(salesAdjustmentDao, Mockito.never()).createSalesAdjustment(any(SaleValueAdjustment.class));
        Mockito.verify(salesAdjustmentDao, Mockito.never()).readSalesAdjustments();
        Mockito.verify(salesSummaryDao, Mockito.times(1)).readSalesSummaries();
    }

    @Test
    public void givenProductMissing_whenSendSalesMessage_thenCheckSaleFailsToConstruct() {
        try {
            mvc.perform(post("/api/sale")
                    .content("{ \"value\" : \"20\"" +
                            "}")
                    .contentType(MediaType.APPLICATION_JSON));
        } catch (Exception e) {
            assertEquals("The product name is missing",
                    Optional.ofNullable(e.getCause())
                            .map(Throwable::getCause)
                            .map(Throwable::getCause)
                            .map(Throwable::getMessage)
                            .orElse("Failed to get the nested cause from the exception"));
        }
    }

    @Test
    public void givenValueMissing_whenSendSalesMessage_thenCheckSaleFailsToConstruct() {
        try {
            mvc.perform(post("/api/sale")
                    .content("{ \"product\" : \"Apple\"" +
                            "}")
                    .contentType(MediaType.APPLICATION_JSON));
        } catch (Exception e) {
            assertEquals("The value of the product is missing",
                    Optional.ofNullable(e.getCause())
                            .map(Throwable::getCause)
                            .map(Throwable::getCause)
                            .map(Throwable::getMessage)
                            .orElse("Failed to get the nested cause from the exception"));
        }
    }

}
