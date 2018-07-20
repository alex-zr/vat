package com.vatbox.task.mvc.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Errors {
    CUSTOMER_DOES_NOT_EXIST(-189003, 400, "Customer doesn't exist"),
    INVOICE_ALREADY_EXISTS(-189005, 400, "Invoice already exists"),
    INVALID_REQUEST(-400, 400, "Bad request"),
    INTERNAL_ERROR(-1, 500, "Internal server error");

    private final long numericCode;
    private final int httpStatusCode;
    private final String description;
}
