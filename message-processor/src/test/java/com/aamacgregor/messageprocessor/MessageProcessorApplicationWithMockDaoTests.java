package com.aamacgregor.messageprocessor;

import com.aamacgregor.messageprocessor.controller.SalesController;
import com.aamacgregor.messageprocessor.dao.ISalesAdjustmentDao;
import com.aamacgregor.messageprocessor.dao.ISalesDao;
import com.aamacgregor.messageprocessor.dao.ISalesSummaryDao;
import com.aamacgregor.messageprocessor.model.enums.AdjustmentOperation;
import com.aamacgregor.messageprocessor.model.vo.Sale;
import com.aamacgregor.messageprocessor.model.vo.SaleValueAdjustment;
import com.aamacgregor.messageprocessor.model.vo.SalesSummary;
import com.aamacgregor.messageprocessor.report.IReportConsumer;
import com.aamacgregor.messageprocessor.report.ReportScheduler;
import com.aamacgregor.messageprocessor.report.detail.SalesAdjustmentAsciiReportGenerator;
import com.aamacgregor.messageprocessor.report.detail.SalesAsciiReportGenerator;
import com.aamacgregor.messageprocessor.service.SalesService;
import org.assertj.core.util.Lists;
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
public class MessageProcessorApplicationWithMockDaoTests {

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
                new SalesSummary("Orange", 25, BigDecimal.valueOf(250))
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
                "|Orange |25      |250        |\n" +
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


    @Test
    public void givenTwoDifferentProducts_whenFiftyValidSalesMessages_thenCheckBothReportsAreCorrectlyGenerated() throws Exception {

        Mockito.when(salesSummaryDao.readSalesSummaries()).thenReturn(Arrays.asList(
                new SalesSummary("Apple", 5, BigDecimal.valueOf(100)),
                new SalesSummary("Orange", 25, BigDecimal.valueOf(250))
        )).thenReturn(Arrays.asList(
                new SalesSummary("Apple", 10, BigDecimal.valueOf(200)),
                new SalesSummary("Orange", 50, BigDecimal.valueOf(500))
        )).thenReturn(Arrays.asList(
                new SalesSummary("Apple", 15, BigDecimal.valueOf(300)),
                new SalesSummary("Orange", 75, BigDecimal.valueOf(750))
        )).thenReturn(Arrays.asList(
                new SalesSummary("Apple", 20, BigDecimal.valueOf(400)),
                new SalesSummary("Orange", 100, BigDecimal.valueOf(1000))
        )).thenReturn(Arrays.asList(
                new SalesSummary("Apple", 25, BigDecimal.valueOf(500)),
                new SalesSummary("Orange", 125, BigDecimal.valueOf(1250))
        ));

        Mockito.when(salesAdjustmentDao.readSalesAdjustments()).thenReturn(Lists.emptyList());

        for (int i = 0; i < 25; i++) {
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
                "|Orange |25      |250        |\n" +
                "------------------------------\n");
        Mockito.verify(reportConsumer, Mockito.times(1)).consume("Sales Report\n" +
                "------------------------------\n" +
                "|Product|Quantity|Total Value|\n" +
                "------------------------------\n" +
                "|Apple  |10      |200        |\n" +
                "|Orange |50      |500        |\n" +
                "------------------------------\n");
        Mockito.verify(reportConsumer, Mockito.times(1)).consume("Sales Report\n" +
                "------------------------------\n" +
                "|Product|Quantity|Total Value|\n" +
                "------------------------------\n" +
                "|Apple  |15      |300        |\n" +
                "|Orange |75      |750        |\n" +
                "------------------------------\n");
        Mockito.verify(reportConsumer, Mockito.times(1)).consume("Sales Report\n" +
                "------------------------------\n" +
                "|Product|Quantity|Total Value|\n" +
                "------------------------------\n" +
                "|Apple  |20      |400        |\n" +
                "|Orange |100     |1000       |\n" +
                "------------------------------\n");
        Mockito.verify(reportConsumer, Mockito.times(1)).consume("Sales Report\n" +
                "------------------------------\n" +
                "|Product|Quantity|Total Value|\n" +
                "------------------------------\n" +
                "|Apple  |25      |500        |\n" +
                "|Orange |125     |1250       |\n" +
                "------------------------------\n");
        Mockito.verify(reportConsumer, Mockito.times(1)).consume("Sales Adjustment Report\n" +
                "-----------------------------------------\n" +
                "|Product|Adjustment|Adjustment Operation|\n" +
                "-----------------------------------------\n" +
                "-----------------------------------------\n");
        Mockito.verify(salesDao, Mockito.times(25))
                .createSale(new Sale("Apple", BigDecimal.valueOf(20), 1));
        Mockito.verify(salesDao, Mockito.times(25))
                .createSale(new Sale("Orange", BigDecimal.valueOf(10), 5));
        Mockito.verify(salesDao, Mockito.never()).readSales();
        Mockito.verify(salesAdjustmentDao, Mockito.never()).createSalesAdjustment(any(SaleValueAdjustment.class));
        Mockito.verify(salesAdjustmentDao, Mockito.times(1)).readSalesAdjustments();
        Mockito.verify(salesSummaryDao, Mockito.times(5)).readSalesSummaries();
    }


    @Test
    public void givenTwoDifferentProductsAndTwoMidAdjustments_whenFiftyValidMessages_thenCheckBothReportsAreCorrectlyGenerated() throws Exception {

        Mockito.when(salesSummaryDao.readSalesSummaries()).thenReturn(Arrays.asList(
                new SalesSummary("Apple", 2, BigDecimal.valueOf(40)),
                new SalesSummary("Pear", 4, BigDecimal.valueOf(60)),
                new SalesSummary("Strawberry", 2, BigDecimal.valueOf(10)),
                new SalesSummary("Peach", 2, BigDecimal.valueOf(16)),
                new SalesSummary("Orange", 10, BigDecimal.valueOf(100))
        )).thenReturn(Arrays.asList(
                new SalesSummary("Apple", 4, BigDecimal.valueOf(80)),
                new SalesSummary("Pear", 8, BigDecimal.valueOf(120)),
                new SalesSummary("Strawberry", 4, BigDecimal.valueOf(20)),
                new SalesSummary("Peach", 4, BigDecimal.valueOf(32)),
                new SalesSummary("Orange", 20, BigDecimal.valueOf(200))
        )).thenReturn(Arrays.asList(
                new SalesSummary("Apple", 6, BigDecimal.valueOf(70)),
                new SalesSummary("Pear", 10, BigDecimal.valueOf(130)),
                new SalesSummary("Strawberry", 5, BigDecimal.valueOf(25)),
                new SalesSummary("Peach", 5, BigDecimal.valueOf(40)),
                new SalesSummary("Orange", 30, BigDecimal.valueOf(425.0))
        )).thenReturn(Arrays.asList(
                new SalesSummary("Apple", 7, BigDecimal.valueOf(125)),
                new SalesSummary("Pear", 14, BigDecimal.valueOf(190)),
                new SalesSummary("Strawberry", 6, BigDecimal.valueOf(30)),
                new SalesSummary("Peach", 7, BigDecimal.valueOf(56)),
                new SalesSummary("Orange", 40, BigDecimal.valueOf(50))
        )).thenReturn(Arrays.asList(
                new SalesSummary("Apple", 9, BigDecimal.valueOf(165)),
                new SalesSummary("Pear", 18, BigDecimal.valueOf(250)),
                new SalesSummary("Strawberry", 8, BigDecimal.valueOf(40)),
                new SalesSummary("Peach", 9, BigDecimal.valueOf(72)),
                new SalesSummary("Orange", 50, BigDecimal.valueOf(150))
        ));

        Mockito.when(salesAdjustmentDao.readSalesAdjustments()).thenReturn(Arrays.asList(
                new SaleValueAdjustment("Apple", BigDecimal.valueOf(10), AdjustmentOperation.SUBTRACT),
                new SaleValueAdjustment("Orange", BigDecimal.valueOf(1.5), AdjustmentOperation.MULTIPLY),
                new SaleValueAdjustment("Pear", BigDecimal.valueOf(2), AdjustmentOperation.SUBTRACT),
                new SaleValueAdjustment("Apple", BigDecimal.valueOf(5), AdjustmentOperation.ADD),
                new SaleValueAdjustment("Orange", BigDecimal.valueOf(100), AdjustmentOperation.SUBTRACT)
        ));

        for (int i = 0; i < 10; i++) {

            if (i == 5) {
                mvc.perform(post("/api/adjustment")
                        .content("{ \"product\" : \"Apple\"," +
                                "  \"adjustment\" : \"10\"," +
                                "  \"adjustmentOperation\" : \"SUBTRACT\"" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
                mvc.perform(post("/api/adjustment")
                        .content("{ \"product\" : \"Orange\"," +
                                "  \"adjustment\" : \"1.5\"," +
                                "  \"adjustmentOperation\" : \"MULTIPLY\"" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
                mvc.perform(post("/api/adjustment")
                        .content("{ \"product\" : \"Pear\"," +
                                "  \"adjustment\" : \"2\"," +
                                "  \"adjustmentOperation\" : \"SUBTRACT\"" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());

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
            } else if (i == 7) {
                mvc.perform(post("/api/adjustment")
                        .content("{ \"product\" : \"Apple\"," +
                                "  \"adjustment\" : \"5\"," +
                                "  \"adjustmentOperation\" : \"ADD\"" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
                mvc.perform(post("/api/adjustment")
                        .content("{ \"product\" : \"Orange\"," +
                                "  \"adjustment\" : \"100\"," +
                                "  \"adjustmentOperation\" : \"SUBTRACT\"" +
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

                mvc.perform(post("/api/sale")
                        .content("{ \"product\" : \"Pear\"," +
                                "  \"value\" : \"15\"," +
                                "  \"quantity\" : \"2\"" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());

                mvc.perform(post("/api/sale")
                        .content("{ \"product\" : \"Peach\"," +
                                "  \"value\" : \"8\"" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
            } else {
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

                mvc.perform(post("/api/sale")
                        .content("{ \"product\" : \"Pear\"," +
                                "  \"value\" : \"15\"," +
                                "  \"quantity\" : \"2\"" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());

                mvc.perform(post("/api/sale")
                        .content("{ \"product\" : \"Peach\"," +
                                "  \"value\" : \"8\"" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());

                mvc.perform(post("/api/sale")
                        .content("{ \"product\" : \"Strawberry\"," +
                                "  \"value\" : \"5\"" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
            }
        }

        Mockito.verify(reportConsumer, Mockito.times(1)).consume("Sales Report\n" +
                "---------------------------------\n" +
                "|Product   |Quantity|Total Value|\n" +
                "---------------------------------\n" +
                "|Apple     |2       |40         |\n" +
                "|Pear      |4       |60         |\n" +
                "|Strawberry|2       |10         |\n" +
                "|Peach     |2       |16         |\n" +
                "|Orange    |10      |100        |\n" +
                "---------------------------------\n");
        Mockito.verify(reportConsumer, Mockito.times(1)).consume("Sales Report\n" +
                "---------------------------------\n" +
                "|Product   |Quantity|Total Value|\n" +
                "---------------------------------\n" +
                "|Apple     |4       |80         |\n" +
                "|Pear      |8       |120        |\n" +
                "|Strawberry|4       |20         |\n" +
                "|Peach     |4       |32         |\n" +
                "|Orange    |20      |200        |\n" +
                "---------------------------------\n");
        Mockito.verify(reportConsumer, Mockito.times(1)).consume("Sales Report\n" +
                "---------------------------------\n" +
                "|Product   |Quantity|Total Value|\n" +
                "---------------------------------\n" +
                "|Apple     |6       |70         |\n" +
                "|Pear      |10      |130        |\n" +
                "|Strawberry|5       |25         |\n" +
                "|Peach     |5       |40         |\n" +
                "|Orange    |30      |425.0      |\n" +
                "---------------------------------\n");
        Mockito.verify(reportConsumer, Mockito.times(1)).consume("Sales Report\n" +
                "---------------------------------\n" +
                "|Product   |Quantity|Total Value|\n" +
                "---------------------------------\n" +
                "|Apple     |7       |125        |\n" +
                "|Pear      |14      |190        |\n" +
                "|Strawberry|6       |30         |\n" +
                "|Peach     |7       |56         |\n" +
                "|Orange    |40      |50         |\n" +
                "---------------------------------\n");
        Mockito.verify(reportConsumer, Mockito.times(1)).consume("Sales Report\n" +
                "---------------------------------\n" +
                "|Product   |Quantity|Total Value|\n" +
                "---------------------------------\n" +
                "|Apple     |9       |165        |\n" +
                "|Pear      |18      |250        |\n" +
                "|Strawberry|8       |40         |\n" +
                "|Peach     |9       |72         |\n" +
                "|Orange    |50      |150        |\n" +
                "---------------------------------\n");
        Mockito.verify(reportConsumer, Mockito.times(1)).consume("Sales Adjustment Report\n" +
                "-----------------------------------------\n" +
                "|Product|Adjustment|Adjustment Operation|\n" +
                "-----------------------------------------\n" +
                "|Apple  |10        |SUBTRACT            |\n" +
                "|Orange |1.5       |MULTIPLY            |\n" +
                "|Pear   |2         |SUBTRACT            |\n" +
                "|Apple  |5         |ADD                 |\n" +
                "|Orange |100       |SUBTRACT            |\n" +
                "-----------------------------------------\n");
        Mockito.verify(salesDao, Mockito.times(9))
                .createSale(new Sale("Apple", BigDecimal.valueOf(20), 1));
        Mockito.verify(salesDao, Mockito.times(10))
                .createSale(new Sale("Orange", BigDecimal.valueOf(10), 5));
        Mockito.verify(salesDao, Mockito.times(9))
                .createSale(new Sale("Pear", BigDecimal.valueOf(15), 2));
        Mockito.verify(salesDao, Mockito.times(9))
                .createSale(new Sale("Peach", BigDecimal.valueOf(8), 1));
        Mockito.verify(salesDao, Mockito.times(8))
                .createSale(new Sale("Strawberry", BigDecimal.valueOf(5), 1));
        Mockito.verify(salesDao, Mockito.never()).readSales();
        Mockito.verify(salesAdjustmentDao, Mockito.times(1)).createSalesAdjustment(new SaleValueAdjustment("Apple", BigDecimal.valueOf(10), AdjustmentOperation.SUBTRACT));
        Mockito.verify(salesAdjustmentDao, Mockito.times(1)).readSalesAdjustments();
        Mockito.verify(salesSummaryDao, Mockito.times(5)).readSalesSummaries();
    }

}

