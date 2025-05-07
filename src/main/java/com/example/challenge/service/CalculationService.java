package com.example.challenge.service;

import com.example.challenge.dto.CalculationRequest;
import com.example.challenge.dto.CalculationResponse;
import com.example.challenge.exception.PercentageUnavailableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service for performing calculations with dynamic percentage adjustments.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CalculationService {

    private final PercentageClient percentageClient;

    /**
     * Performs the calculation with the given numbers and applies the dynamic percentage.
     *
     * @param request the calculation request containing two numbers
     * @return the calculation result with the applied percentage
     * @throws PercentageUnavailableException if the percentage service is unavailable
     */
    public CalculationResponse calculate(CalculationRequest request) {
        log.debug("Calculating result for numbers: {}, {}", request.num1(), request.num2());
        
        Double percentage = percentageClient.getPercentage();
        Double sum = request.num1() + request.num2();
        Double result = sum * (1 + percentage);
        
        log.debug("Calculation result: {} (percentage: {})", result, percentage);
        return new CalculationResponse(result, percentage);
    }
} 