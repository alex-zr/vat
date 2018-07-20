package com.vatbox.task.service;

import com.vatbox.task.persistence.domain.Invoice;
import com.vatbox.task.mvc.api.InvoiceRequest;

import java.math.BigDecimal;

public interface InvoiceService {
    Invoice create(InvoiceRequest invoiceRequest);

    BigDecimal getTotalAmount(Long customerId);

    BigDecimal getTotalVat(Long customerId);
}
