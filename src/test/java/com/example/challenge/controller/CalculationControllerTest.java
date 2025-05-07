package com.example.challenge.controller;

import com.example.challenge.dto.CalculationRequest;
import com.example.challenge.dto.CalculationResponse;
import com.example.challenge.service.CalculationService;
import com.example.challenge.service.LogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CalculationController.class)
class CalculationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CalculationService calculationService;

    @MockBean
    private LogService logService;

    private CalculationRequest request;
    private CalculationResponse response;

    @BeforeEach
    void setUp() {
        request = new CalculationRequest(10.0, 20.0);
        response = new CalculationResponse(33.0, 0.10);
    }

    @Test
    void calculate_ShouldReturnResult_WhenRequestIsValid() throws Exception {
        // Arrange
        when(calculationService.calculate(any(CalculationRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(33.0))
                .andExpect(jsonPath("$.percentage").value(0.10));

        verify(calculationService).calculate(request);
        verify(logService).saveLog(any());
    }

    @Test
    void calculate_ShouldReturnBadRequest_WhenRequestIsInvalid() throws Exception {
        // Arrange
        request = new CalculationRequest(-1.0, 20.0);

        // Act & Assert
        mockMvc.perform(post("/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
} 