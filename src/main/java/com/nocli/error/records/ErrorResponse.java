package com.nocli.error.records;

public record ErrorResponse(
        int status,
        String type,
        String message,
        String stackTrace
) {
}
