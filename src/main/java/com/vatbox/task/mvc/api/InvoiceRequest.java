package com.vatbox.task.mvc.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceRequest {
    @NotNull
    @JsonProperty("customer_id")
    long customerId;

    @NotNull
    @JsonProperty("invoice_id")
    long invoiceId;

    @NotNull
    BigDecimal amount;

    @NotNull
    BigDecimal vat;
}