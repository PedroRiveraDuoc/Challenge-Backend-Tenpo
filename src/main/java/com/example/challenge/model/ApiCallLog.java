package com.example.challenge.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA entity for storing API call logs.
 */
@Entity
@Table(name = "api_call_logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiCallLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private String endpoint;

    @Column(name = "parameters_json", columnDefinition = "TEXT")
    private String parametersJson;

    @Column(name = "response_json", columnDefinition = "TEXT")
    private String responseJson;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ApiCallStatus status;

    public enum ApiCallStatus {
        SUCCESS,
        ERROR
    }
} 