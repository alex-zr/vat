package com.vatbox.task.mvc.exception;

import java.io.Serializable;
import java.util.UUID;

public class ErrorResponse implements Serializable {
    public final long code;
    public final String incidentId;
    public final String message;

    public ErrorResponse(long code, String message) {
        this.code = code;
        this.message = message;
        incidentId = UUID.randomUUID().toString();
    }
}