package com.vatbox.task.mvc.exception;

public class CustomerAbsenceException extends RuntimeException {
    public CustomerAbsenceException() {
    }

    public CustomerAbsenceException(String message) {
        super(message);
    }
}
