package org.example.warehouseservice.exceptions.model;

import lombok.Builder;

import java.time.Instant;

@Builder
public record ErrorResponse(
        String message,
        int statusCode,
        String timeStamp) {

    public static ErrorResponse toErrorResponse(String message, int statusCode) {
        return ErrorResponse.builder()
                .message(message)
                .statusCode(statusCode)
                .timeStamp(Instant.now().toString())
                .build();
    }
}
