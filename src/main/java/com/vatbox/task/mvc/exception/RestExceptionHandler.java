package com.vatbox.task.mvc.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

import static com.vatbox.task.mvc.exception.Errors.CUSTOMER_DOES_NOT_EXIST;
import static com.vatbox.task.mvc.exception.Errors.INTERNAL_ERROR;
import static com.vatbox.task.mvc.exception.Errors.INVOICE_ALREADY_EXISTS;


@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e, HttpServletRequest request) {
        return handleError(INTERNAL_ERROR, e, request);
    }

    @ExceptionHandler(CustomerAbsenceException.class)
    public ResponseEntity<ErrorResponse> handleCustomerAbsenceException(Exception e, HttpServletRequest request) {
        return handleError(CUSTOMER_DOES_NOT_EXIST, e, request);
    }

    @ExceptionHandler(InvoiceExistsException.class)
    public ResponseEntity<ErrorResponse> handleInvoiceExistsException(Exception e, HttpServletRequest request) {
        return handleError(INVOICE_ALREADY_EXISTS, e, request);
    }

    protected ResponseEntity<ErrorResponse> handleError(Errors errorCode, Exception e, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getNumericCode(), errorCode.getDescription());
        log.error("Exception occurred incidentId={}: {}", errorResponse.incidentId, e.getMessage(), e);

        return ResponseEntity.status(errorCode.getHttpStatusCode()).body(errorResponse);
    }
}
