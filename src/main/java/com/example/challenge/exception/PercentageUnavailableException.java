package com.example.challenge.exception;

/**
 * Exception thrown when the percentage service is unavailable.
 */
public class PercentageUnavailableException extends RuntimeException {

    public PercentageUnavailableException(String message) {
        super(message);
    }

    public PercentageUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
} 