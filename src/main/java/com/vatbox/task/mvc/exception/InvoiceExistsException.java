package com.vatbox.task.mvc.exception;

public class InvoiceExistsException extends RuntimeException {
    public InvoiceExistsException() {
    }

    public InvoiceExistsException(String message) {
        super(message);
    }
}
