package com.vatbox.task.mvc.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class TotalCustomerResponse {
    @JsonProperty("customer_id")
    long customerId;

    @JsonProperty("total_amount")
    BigDecimal totalAmount;

    @JsonProperty("total_vat")
    BigDecimal totalVat;
}
