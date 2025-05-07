package com.example.challenge.dto;

/**
 * DTO for calculation response containing the result of the calculation.
 */
public record CalculationResponse(
    Double result,
    Double percentage
) {} 