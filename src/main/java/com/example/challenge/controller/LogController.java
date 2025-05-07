package com.example.challenge.controller;

import com.example.challenge.dto.ApiCallLogDto;
import com.example.challenge.service.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for retrieving API call logs.
 */
@Slf4j
@RestController
@RequestMapping("/logs")
@RequiredArgsConstructor
@Tag(name = "Logs", description = "API call logs endpoints")
public class LogController {

    private final LogService logService;

    /**
     * Retrieves a paginated list of API call logs.
     *
     * @param page page number (0-based)
     * @param size page size
     * @return page of API call logs
     */
    @GetMapping
    @Operation(summary = "Get paginated API call logs")
    public ResponseEntity<Page<ApiCallLogDto>> getLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.debug("Retrieving logs page {} with size {}", page, size);
        
        Page<ApiCallLogDto> logs = logService.getLogs(PageRequest.of(page, size));
        return ResponseEntity.ok(logs);
    }
} 