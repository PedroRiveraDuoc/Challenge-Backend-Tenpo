package com.example.challenge.service;

import com.example.challenge.exception.PercentageUnavailableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Client for fetching percentage values from an external service.
 * Implements caching to reduce external service calls.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PercentageClient {

    private final WebClient webClient;

    @Value("${percentage.service.url:http://localhost:9000/percentage}")
    private String percentageServiceUrl;

    /**
     * Fetches the current percentage value from the external service.
     * Results are cached for 30 minutes.
     *
     * @return the percentage value as a double
     * @throws PercentageUnavailableException if the service is unavailable
     */
    @Cacheable(value = "percentage", key = "'dynamicPercentage'")
    public Double getPercentage() {
        log.debug("Fetching percentage from external service");
        
        try {
            return webClient.get()
                    .uri(percentageServiceUrl)
                    .retrieve()
                    .bodyToMono(PercentageResponse.class)
                    .map(PercentageResponse::value)
                    .onErrorResume(e -> {
                        log.error("Error fetching percentage: {}", e.getMessage());
                        return Mono.error(new PercentageUnavailableException("Percentage service unavailable"));
                    })
                    .block();
        } catch (Exception e) {
            log.error("Failed to fetch percentage, using cached value if available", e);
            throw new PercentageUnavailableException("Percentage service unavailable", e);
        }
    }

    public record PercentageResponse(Double value) {}
} 