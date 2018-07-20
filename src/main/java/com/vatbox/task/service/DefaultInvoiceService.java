package com.vatbox.task.service;

import com.vatbox.task.mvc.api.InvoiceRequest;
import com.vatbox.task.mvc.exception.CustomerAbsenceException;
import com.vatbox.task.mvc.exception.InvoiceExistsException;
import com.vatbox.task.persistence.domain.Customer;
import com.vatbox.task.persistence.domain.Invoice;
import com.vatbox.task.persistence.repository.CustomerRepository;
import com.vatbox.task.persistence.repository.InvoiceRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class DefaultInvoiceService implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final CustomerRepository customerRepository;

    @Override
    public Invoice create(InvoiceRequest invoiceRequest) {

        Optional<Customer> customer = getCustomerAndCheckIfExists(invoiceRequest);
        checkIfInvoiceAbsent(invoiceRequest.getInvoiceId());
        Invoice newInvoice = createNewInvoice(invoiceRequest, customer);
        invoiceRepository.save(newInvoice);

        return newInvoice;
    }

    @Override
    public BigDecimal getTotalAmount(Long customerId) {
        return invoiceRepository.findAllByCustomerId(customerId).stream()
                .map(Invoice::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getTotalVat(Long customerId) {
        return invoiceRepository.findAllByCustomerId(customerId).stream()
                .map(Invoice::getVat)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Invoice createNewInvoice(InvoiceRequest invoiceRequest, Optional<Customer> customer) {
        Invoice newInvoice = Invoice.builder().build();
        newInvoice.setAmount(invoiceRequest.getAmount());
        newInvoice.setCreated(new Date());
        newInvoice.setCustomer(customer.get());
        newInvoice.setVat(invoiceRequest.getVat());
        return newInvoice;
    }

    private void checkIfInvoiceAbsent(Long invoiceId) {
        invoiceRepository.findById(invoiceId)
                .ifPresent(in -> {
                    String message = String.format("Invoice with id %s is already exists", invoiceId);
                    log.error(message);
                    throw new InvoiceExistsException(message);
                });
    }

    private Optional<Customer> getCustomerAndCheckIfExists(InvoiceRequest invoiceRequest) {
        Optional<Customer> customer = customerRepository.findById(invoiceRequest.getCustomerId());
        if (!customer.isPresent()) {
            String message = String.format("Customer with id %s is absent", invoiceRequest.getCustomerId());
            log.error(message);
            throw new CustomerAbsenceException(message);
        }
        return customer;
    }
}
