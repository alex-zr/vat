package com.vatbox.task.service;

import com.vatbox.task.mvc.api.InvoiceRequest;
import com.vatbox.task.mvc.exception.CustomerAbsenceException;
import com.vatbox.task.mvc.exception.InvoiceExistsException;
import com.vatbox.task.persistence.domain.Customer;
import com.vatbox.task.persistence.domain.Invoice;
import com.vatbox.task.persistence.repository.CustomerRepository;
import com.vatbox.task.persistence.repository.InvoiceRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultInvoiceServiceTest {

    private static final Long CUSTOMER_ID = 44444L;
    private static final Long INVOICE_ID = 55555L;
    private static final BigDecimal AMOUNT = BigDecimal.valueOf(1234.1234);
    private static final BigDecimal VAT = BigDecimal.valueOf(12.12);
    private static final BigDecimal TOTAL_AMOUNT = BigDecimal.valueOf(2468.2468);
    private static final BigDecimal TOTAL_VAT = BigDecimal.valueOf(24.24);

    @Mock
    private InvoiceRepository invoiceRepository;
    @Mock
    private CustomerRepository customerRepository;
    @InjectMocks
    private DefaultInvoiceService invoiceService;

    private InvoiceRequest invoiceRequest;

    @Before
    public void setUp() {
        invoiceRequest = InvoiceRequest.builder()
                .customerId(CUSTOMER_ID)
                .invoiceId(INVOICE_ID)
                .amount(AMOUNT)
                .build();
    }

    @Test
    public void createSuccessful() {
        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.of(Customer.builder().build()));

        invoiceService.create(invoiceRequest);
    }

    @Test(expected = CustomerAbsenceException.class)
    public void createNoCustomer() {
        when(customerRepository.findById(CUSTOMER_ID)).thenThrow(new CustomerAbsenceException());

        invoiceService.create(invoiceRequest);
    }

    @Test(expected = InvoiceExistsException.class)
    public void createExistingInvoice() {
        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.of(Customer.builder().build()));
        when(invoiceRepository.findById(INVOICE_ID)).thenThrow(new InvoiceExistsException());

        invoiceService.create(invoiceRequest);
    }

    @Test
    public void getTotalAmountSuccessful()  {
        Invoice invoice = Invoice.builder()
                .amount(AMOUNT)
                .build();

        List<Invoice> invoices = Arrays.asList(invoice, invoice);
        when(invoiceRepository.findAllByCustomerId(CUSTOMER_ID)).thenReturn(invoices);

        assertEquals(TOTAL_AMOUNT, invoiceService.getTotalAmount(CUSTOMER_ID));
    }

    @Test
    public void getTotalAmountNoInvoices() {
        when(invoiceRepository.findAllByCustomerId(CUSTOMER_ID)).thenReturn(Arrays.asList());

        assertEquals(BigDecimal.ZERO, invoiceService.getTotalAmount(CUSTOMER_ID));
    }

    @Test
    public void getTotalVatSuccessful() {
        Invoice invoice = Invoice.builder()
                .vat(VAT)
                .build();

        List<Invoice> invoices = Arrays.asList(invoice, invoice);
        when(invoiceRepository.findAllByCustomerId(CUSTOMER_ID)).thenReturn(invoices);

        assertEquals(TOTAL_VAT, invoiceService.getTotalVat(CUSTOMER_ID));
    }
}