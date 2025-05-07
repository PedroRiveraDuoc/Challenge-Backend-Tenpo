package com.example.challenge.service;

import com.example.challenge.exception.PercentageUnavailableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PercentageClientTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private PercentageClient percentageClient;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(percentageClient, "percentageServiceUrl", "http://test-url/percentage");
        
        // Chain the WebClient call mocks properly
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(String.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    void getPercentage_ShouldReturnValue_WhenServiceResponds() {
        // Arrange
        when(responseSpec.bodyToMono(PercentageClient.PercentageResponse.class))
                .thenReturn(Mono.just(new PercentageClient.PercentageResponse(0.10)));

        // Act
        Double result = percentageClient.getPercentage();

        // Assert
        assertThat(result).isEqualTo(0.10);
    }

    @Test
    void getPercentage_ShouldThrowException_WhenServiceFails() {
        // Arrange
        when(responseSpec.bodyToMono(PercentageClient.PercentageResponse.class))
                .thenReturn(Mono.error(new RuntimeException("Service error")));

        // Act & Assert
        assertThatThrownBy(() -> percentageClient.getPercentage())
                .isInstanceOf(PercentageUnavailableException.class)
                .hasMessageContaining("Percentage service unavailable");
    }

    @Test
    void getPercentage_ShouldThrowException_WhenServiceThrowsException() {
        // Arrange
        when(responseSpec.bodyToMono(PercentageClient.PercentageResponse.class))
                .thenThrow(new RuntimeException("Unexpected error"));

        // Act & Assert
        assertThatThrownBy(() -> percentageClient.getPercentage())
                .isInstanceOf(PercentageUnavailableException.class)
                .hasMessageContaining("Percentage service unavailable");
    }
}
