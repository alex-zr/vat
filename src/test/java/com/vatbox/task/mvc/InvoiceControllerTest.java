package com.vatbox.task.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.vatbox.task.mvc.api.InvoiceRequest;
import com.vatbox.task.mvc.api.TotalCustomerResponse;
import com.vatbox.task.mvc.exception.CustomerAbsenceException;
import com.vatbox.task.mvc.exception.Errors;
import com.vatbox.task.mvc.exception.InvoiceExistsException;
import com.vatbox.task.service.InvoiceService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(InvoiceController.class)
public class InvoiceControllerTest {

    private static final Long CUSTOMER_ID = 44444L;
    private static final Long INVOICE_ID = 55555L;
    private static final BigDecimal AMOUNT = BigDecimal.valueOf(1234.1234);
    private static final BigDecimal VAT = BigDecimal.valueOf(11.11);
    private static final BigDecimal TOTAL_AMOUNT = BigDecimal.TEN;
    private static final BigDecimal TOTAL_VAT = BigDecimal.ONE;
    private static final String CREATE_INVOICE_PATH = "/invoice";
    private static final String USER_NAME = "test";
    private static final String PASSWORD = "test";

    @Autowired
    private WebApplicationContext webApplicationContext;
    @MockBean
    private InvoiceService invoiceService;

    private MockMvc mockMvc;
    private InvoiceRequest invoiceRequest;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();

        invoiceRequest = InvoiceRequest.builder()
                .customerId(CUSTOMER_ID)
                .invoiceId(INVOICE_ID)
                .amount(AMOUNT)
                .vat(VAT)
                .build();

        when(invoiceService.getTotalAmount(CUSTOMER_ID)).thenReturn(TOTAL_AMOUNT);
        when(invoiceService.getTotalVat(CUSTOMER_ID)).thenReturn(TOTAL_VAT);
    }

    @Test
    public void createNewInvoiceSuccessful() throws Exception {
        mockMvc.perform(
                post(CREATE_INVOICE_PATH)
                        .content(asJsonString(invoiceRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        //.with(httpBasic(USER_NAME, PASSWORD))
        )
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(TotalCustomerResponse.builder()
                        .customerId(CUSTOMER_ID)
                        .totalAmount(TOTAL_AMOUNT)
                        .totalVat(TOTAL_VAT)
                        .build())));
    }

    @Test
    public void createNewInvoiceNoCustomer() throws Exception {
        when(invoiceService.create(invoiceRequest)).thenThrow(new CustomerAbsenceException());
        mockMvc.perform(
                post(CREATE_INVOICE_PATH)
                        .content(asJsonString(invoiceRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(httpBasic(USER_NAME, PASSWORD))
        )
                .andExpect(status().is(Errors.CUSTOMER_DOES_NOT_EXIST.getHttpStatusCode()));
    }

    @Test
    public void createNewInvoiceExistingInvoice() throws Exception {
        when(invoiceService.create(invoiceRequest)).thenThrow(new InvoiceExistsException());
        mockMvc.perform(
                post(CREATE_INVOICE_PATH)
                        .content(asJsonString(invoiceRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(httpBasic(USER_NAME, PASSWORD))
        )
                .andExpect(status().is(Errors.INVOICE_ALREADY_EXISTS.getHttpStatusCode()));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().registerModule(new Jdk8Module()).writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}