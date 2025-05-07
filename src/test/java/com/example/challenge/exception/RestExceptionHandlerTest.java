package com.example.challenge.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestExceptionHandlerTest {

    @Mock
    private HttpServletRequest request;

    private RestExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new RestExceptionHandler();
        when(request.getRequestURI()).thenReturn("/test");
    }

    @Test
    void handlePercentageUnavailable_ShouldReturnServiceUnavailable() {
        // Arrange
        PercentageUnavailableException ex = new PercentageUnavailableException("Service unavailable");

        // Act
        ResponseEntity<ErrorResponse> response = exceptionHandler.handlePercentageUnavailable(ex, request);

        // Assert
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertEquals("Service Unavailable", response.getBody().error());
        assertEquals("Service unavailable", response.getBody().message());
    }

    @Test
    void handleValidationExceptions_ShouldReturnBadRequest() {
        // Arrange
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        FieldError fieldError = new FieldError("object", "field", "default message");
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));
        when(ex.getBindingResult()).thenReturn(bindingResult);

        // Act
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleValidationExceptions(ex, request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Bad Request", response.getBody().error());
        assertEquals("field: default message", response.getBody().message());
    }

    @Test
    void handleConstraintViolation_ShouldReturnBadRequest() {
        // Arrange
        Set<ConstraintViolation<?>> violations = new HashSet<>();
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        Path path = mock(Path.class);
        when(violation.getPropertyPath()).thenReturn(path);
        when(path.toString()).thenReturn("");
        when(violation.getMessage()).thenReturn("invalid value");
        violations.add(violation);

        ConstraintViolationException ex = new ConstraintViolationException(violations);

        // Act
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleConstraintViolation(ex, request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Bad Request", response.getBody().error());
        assertEquals(": invalid value", response.getBody().message());
    }

    @Test
    void handleGenericException_ShouldReturnInternalServerError() {
        // Arrange
        Exception ex = new RuntimeException("Unexpected error");

        // Act
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleGenericException(ex, request);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal Server Error", response.getBody().error());
        assertEquals("An unexpected error occurred", response.getBody().message());
    }
} 