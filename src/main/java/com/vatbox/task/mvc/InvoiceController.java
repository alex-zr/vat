package com.vatbox.task.mvc;

import com.vatbox.task.mvc.api.InvoiceRequest;
import com.vatbox.task.mvc.api.TotalCustomerResponse;
import com.vatbox.task.mvc.exception.ErrorResponse;
import com.vatbox.task.service.InvoiceService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.math.BigDecimal;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Log
@AllArgsConstructor
@RestController
@RequestMapping("/invoice")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @ApiOperation("Returns customer total invoices info for customer")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "-189003: Customer doesn't exist", response = ErrorResponse.class),
            @ApiResponse(code = 400, message = "-189005: Invoice already exists", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "-1: Internal error", response = ErrorResponse.class),
            @ApiResponse(code = 200, message = "OK", response = Long.class)
    })

    @PostMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public TotalCustomerResponse createNewInvoice(@RequestBody InvoiceRequest invoiceRequest) {
        log.info("New invoice: " + invoiceRequest);

        invoiceService.create(invoiceRequest);

        long customerId = invoiceRequest.getCustomerId();
        BigDecimal totalAmount = invoiceService.getTotalAmount(customerId);
        BigDecimal totalVat = invoiceService.getTotalVat(customerId);

        return TotalCustomerResponse.builder()
                .customerId(customerId)
                .totalAmount(totalAmount)
                .totalVat(totalVat)
                .build();
    }
}