package com.example.challenge.service;

import com.example.challenge.dto.CalculationRequest;
import com.example.challenge.dto.CalculationResponse;
import com.example.challenge.exception.PercentageUnavailableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CalculationServiceTest {

    @Mock
    private PercentageClient percentageClient;

    @InjectMocks
    private CalculationService calculationService;

    private CalculationRequest request;

    @BeforeEach
    void setUp() {
        request = new CalculationRequest(10.0, 20.0);
    }

    @Test
    void calculate_ShouldReturnCorrectResult_WhenPercentageIsAvailable() {
        // Arrange
        when(percentageClient.getPercentage()).thenReturn(0.10);

        // Act
        CalculationResponse response = calculationService.calculate(request);

        // Assert
        assertEquals(33.0, response.result());
        assertEquals(0.10, response.percentage());
    }

    @Test
    void calculate_ShouldThrowException_WhenPercentageServiceIsUnavailable() {
        // Arrange
        when(percentageClient.getPercentage())
                .thenThrow(new PercentageUnavailableException("Service unavailable"));

        // Act & Assert
        assertThrows(PercentageUnavailableException.class, () -> calculationService.calculate(request));
    }
} 