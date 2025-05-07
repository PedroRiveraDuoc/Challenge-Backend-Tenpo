package com.example.challenge.dto;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for API call logs containing information about each API request.
 */
public record ApiCallLogDto(
    UUID id,
    LocalDateTime timestamp,
    String endpoint,
    String parametersJson,
    String responseJson,
    String status
) {} 