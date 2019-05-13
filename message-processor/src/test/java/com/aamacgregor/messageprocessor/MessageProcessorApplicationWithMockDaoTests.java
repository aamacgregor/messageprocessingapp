package com.aamacgregor.messageprocessor;

import com.aamacgregor.messageprocessor.accessor.dao.detail.InMemorySalesAdjustmentStorage;
import com.aamacgregor.messageprocessor.accessor.dao.detail.InMemorySalesStorage;
import com.aamacgregor.messageprocessor.accessor.service.SalesAccessorService;
import com.aamacgregor.messageprocessor.accessor.service.SalesAdjustmentAccessorService;
import com.aamacgregor.messageprocessor.controller.SalesController;
import com.aamacgregor.messageprocessor.processor.ProcessManager;
import com.aamacgregor.messageprocessor.report.IReportConsumer;
import com.aamacgregor.messageprocessor.report.ReportScheduler;
import com.aamacgregor.messageprocessor.report.detail.AsciiSalesAdjustmentReportGenerator;
import com.aamacgregor.messageprocessor.report.detail.AsciiSalesReportGenerator;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest({SalesController.class, SalesService.class, ReportScheduler.class,
        SalesAccessorService.class, SalesAdjustmentAccessorService.class,
        AsciiSalesAdjustmentReportGenerator.class, AsciiSalesReportGenerator.class,
        ProcessManager.class, InMemorySalesAdjustmentStorage.class, InMemorySalesStorage.class})
public class MessageProcessorApplicationWithMockDaoTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private IReportConsumer reportConsumer;

    @Test
    public void givenOneProduct_whenFiftyOneValidSalesMessage_thenCheckItFailsToProcessTheFiftyFirst() throws Exception {

        String product = "Apple";
        String value = "20";
        String adjustmentOperation = "MULTIPLY";

        //When
        for (int i = 0; i < 51; ++i) {
            if (i != 0 && i % 20 == 0) {
                mvc.perform(post("/api/adjustment")
                        .content("{ \"product\" : \"" + product + "\"," +
                                "  \"adjustment\" : \"1.5\"," +
                                "  \"adjustmentOperation\" : \"" + adjustmentOperation + "\"" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
            } else {
                mvc.perform(post("/api/sale")
                        .content("{ \"product\" : \"" + product + "\"," +
                                "  \"value\" : \"" + value + "\"" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(i < 50 ? status().isOk() : status().isUnprocessableEntity());
            }

            if (i == 35) {
                product = "Orange";
                value = "10";
                adjustmentOperation = "SUBTRACT";
            }
        }

        //Then
        Mockito.verify(reportConsumer, Mockito.times(1)).consume("Sales Report\n" +
                "------------------------------\n" +
                "|Product|Quantity|Total Value|\n" +
                "------------------------------\n" +
                "|Apple  |10      |200        |\n" +
                "------------------------------\n");
        Mockito.verify(reportConsumer, Mockito.times(1)).consume("Sales Report\n" +
                "------------------------------\n" +
                "|Product|Quantity|Total Value|\n" +
                "------------------------------\n" +
                "|Apple  |20      |400        |\n" +
                "------------------------------\n");
        Mockito.verify(reportConsumer, Mockito.times(1)).consume("Sales Report\n" +
                "------------------------------\n" +
                "|Product|Quantity|Total Value|\n" +
                "------------------------------\n" +
                "|Apple  |29      |780.0      |\n" +
                "------------------------------\n");
        Mockito.verify(reportConsumer, Mockito.times(1)).consume("Sales Report\n" +
                "------------------------------\n" +
                "|Product|Quantity|Total Value|\n" +
                "------------------------------\n" +
                "|Apple  |35      |900.0      |\n" +
                "|Orange |4       |40         |\n" +
                "------------------------------\n");
        Mockito.verify(reportConsumer, Mockito.times(1)).consume("Application pausing - stopping processing of future messages!");
        Mockito.verify(reportConsumer, Mockito.times(1)).consume("Sales Adjustment Report\n" +
                "-----------------------------------------\n" +
                "|Product|Adjustment|Adjustment Operation|\n" +
                "-----------------------------------------\n" +
                "|Apple  |1.5       |MULTIPLY            |\n" +
                "|Orange |1.5       |SUBTRACT            |\n" +
                "-----------------------------------------\n");
        Mockito.verify(reportConsumer, Mockito.times(1)).consume("Sales Report\n" +
                "------------------------------\n" +
                "|Product|Quantity|Total Value|\n" +
                "------------------------------\n" +
                "|Apple  |35      |900.0      |\n" +
                "|Orange |13      |124.0      |\n" +
                "------------------------------\n");
        Mockito.verify(reportConsumer, Mockito.times(1)).consume("Message refused - the application has stopped processing messages");

    }

}

