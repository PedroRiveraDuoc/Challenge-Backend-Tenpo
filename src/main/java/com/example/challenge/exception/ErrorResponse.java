package com.example.challenge.exception;

import java.time.LocalDateTime;

/**
 * DTO for error responses.
 */
public record ErrorResponse(
    LocalDateTime timestamp,
    int status,
    String error,
    String message,
    String path
) {} 