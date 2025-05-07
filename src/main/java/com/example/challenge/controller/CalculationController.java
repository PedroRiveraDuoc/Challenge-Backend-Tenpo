package com.example.challenge.controller;

import com.example.challenge.dto.ApiCallLogDto;
import com.example.challenge.dto.CalculationRequest;
import com.example.challenge.dto.CalculationResponse;
import com.example.challenge.service.CalculationService;
import com.example.challenge.service.LogService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling calculation requests.
 */
@Slf4j
@RestController
@RequestMapping("/calculate")
@RequiredArgsConstructor
@Tag(name = "Calculation", description = "Calculation API endpoints")
public class CalculationController {

    private final CalculationService calculationService;
    private final LogService logService;
    private final ObjectMapper objectMapper;

    /**
     * Calculates the result of two numbers with a dynamic percentage adjustment.
     *
     * @param request the calculation request
     * @return the calculation result
     */
    @PostMapping
    @Operation(summary = "Calculate result with dynamic percentage")
    public ResponseEntity<CalculationResponse> calculate(@Valid @RequestBody CalculationRequest request) {
        log.info("Received calculation request: {}", request);
        try {
            CalculationResponse response = calculationService.calculate(request);
            logSuccess(request, response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logError(request, e);
            throw e;
        }
    }

    private void logSuccess(CalculationRequest request, CalculationResponse response) {
        try {
            logService.saveLog(new ApiCallLogDto(
                    null,
                    null,
                    "/calculate",
                    objectMapper.writeValueAsString(request),
                    objectMapper.writeValueAsString(response),
                    "SUCCESS"
            ));
        } catch (JsonProcessingException e) {
            log.error("Error serializing log data", e);
        }
    }

    private void logError(CalculationRequest request, Exception e) {
        try {
            logService.saveLog(new ApiCallLogDto(
                    null,
                    null,
                    "/calculate",
                    objectMapper.writeValueAsString(request),
                    e.getMessage(),
                    "ERROR"
            ));
        } catch (JsonProcessingException ex) {
            log.error("Error serializing log data", ex);
        }
    }
} 