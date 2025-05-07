package com.example.challenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Main application class for the Challenge Backend.
 * Enables caching and async processing capabilities.
 */
@SpringBootApplication
@EnableCaching
@EnableAsync
public class ChallengeBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChallengeBackendApplication.class, args);
    }
} 