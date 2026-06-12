package com.aivle.bookapp.exception;

import java.time.LocalDateTime;

public class ErrorResponse {
    private final int status;
    private final String message;
    private final Object details;
    private final LocalDateTime timestamp = LocalDateTime.now();

    public ErrorResponse(int status, String message, Object details) {
        this.status = status;
        this.message = message;
        this.details = details;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Object getDetails() {
        return details;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
