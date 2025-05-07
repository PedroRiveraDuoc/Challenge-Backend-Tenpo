package com.example.challenge.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * DTO for calculation request containing two numbers to be processed.
 */
public record CalculationRequest(
    @NotNull(message = "First number cannot be null")
    @Positive(message = "First number must be positive")
    Double num1,

    @NotNull(message = "Second number cannot be null")
    @Positive(message = "Second number must be positive")
    Double num2
) {} 