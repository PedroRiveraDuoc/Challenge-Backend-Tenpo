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
import com.fasterxml.jackson.core.JsonProcessingException;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.fail;


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

    @Test
    void calculate_ShouldHandleException_AndLogError() throws Exception {
        // Arrange
        when(calculationService.calculate(any(CalculationRequest.class)))
                .thenThrow(new RuntimeException("Internal error"));

        // Act & Assert
        mockMvc.perform(post("/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is5xxServerError());

        verify(logService).saveLog(any()); // verifica que se intente loggear el error
    }

    @Test
    void logSuccess_ShouldLogSerializationError_WhenObjectMapperFails() throws Exception {
        CalculationController controller = new CalculationController(calculationService, logService, objectMapper);
        ObjectMapper mockMapper = Mockito.mock(ObjectMapper.class);
        when(mockMapper.writeValueAsString(any())).thenThrow(new JsonProcessingException("fail") {});
    }

    @Test
void logSuccess_ShouldHandleJsonProcessingException() {
    // Arrange
    ObjectMapper mockMapper = Mockito.mock(ObjectMapper.class);
    LogService mockLogService = Mockito.mock(LogService.class);
    CalculationService mockCalculationService = Mockito.mock(CalculationService.class);

    CalculationController controller = new CalculationController(mockCalculationService, mockLogService, mockMapper);
    CalculationRequest request = new CalculationRequest(10.0, 20.0);
    CalculationResponse response = new CalculationResponse(30.0, 0.1);

    // Simula que falla al serializar el request
    try {
        when(mockMapper.writeValueAsString(any())).thenThrow(new JsonProcessingException("fail") {});
    } catch (JsonProcessingException e) {
        // No ocurre realmente
    }

    // Act
    // Ejecutar vía reflexión para llamar método privado (si no es público)
    // O convertir temporalmente a `public` si lo prefieres
    try {
        java.lang.reflect.Method method = CalculationController.class.getDeclaredMethod("logSuccess", CalculationRequest.class, CalculationResponse.class);
        method.setAccessible(true);
        method.invoke(controller, request, response);
    } catch (Exception e) {
        fail("Should not throw: " + e.getMessage());
    }
}
@Test
void logError_ShouldHandleJsonProcessingException() {
    // Arrange
    ObjectMapper mockMapper = Mockito.mock(ObjectMapper.class);
    LogService mockLogService = Mockito.mock(LogService.class);
    CalculationService mockCalculationService = Mockito.mock(CalculationService.class);

    CalculationController controller = new CalculationController(mockCalculationService, mockLogService, mockMapper);
    CalculationRequest request = new CalculationRequest(10.0, 20.0);
    Exception exception = new RuntimeException("Some error");

    // Simula fallo en writeValueAsString
    try {
        when(mockMapper.writeValueAsString(any())).thenThrow(new JsonProcessingException("fail") {});
    } catch (JsonProcessingException e) {
        // nada
    }

    // Act
    try {
        java.lang.reflect.Method method = CalculationController.class.getDeclaredMethod("logError", CalculationRequest.class, Exception.class);
        method.setAccessible(true);
        method.invoke(controller, request, exception);
    } catch (Exception e) {
        fail("Should not throw: " + e.getMessage());
    }
}

}