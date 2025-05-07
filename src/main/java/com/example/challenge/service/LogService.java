package com.example.challenge.service;

import com.example.challenge.dto.ApiCallLogDto;
import com.example.challenge.model.ApiCallLog;
import com.example.challenge.repository.ApiCallLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Service for managing API call logs.
 * Handles asynchronous persistence of logs and retrieval of log history.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LogService {

    private final ApiCallLogRepository logRepository;

    /**
     * Asynchronously saves an API call log entry.
     *
     * @param logDto the log entry to save
     */
    @Async
    public void saveLog(ApiCallLogDto logDto) {
        log.debug("Saving API call log for endpoint: {}", logDto.endpoint());
        
        ApiCallLog logEntry = ApiCallLog.builder()
                .endpoint(logDto.endpoint())
                .parametersJson(logDto.parametersJson())
                .responseJson(logDto.responseJson())
                .status(ApiCallLog.ApiCallStatus.valueOf(logDto.status()))
                .build();
        
        logRepository.save(logEntry);
    }

    /**
     * Retrieves a paginated list of API call logs.
     *
     * @param pageable pagination information
     * @return page of API call logs
     */
    public Page<ApiCallLogDto> getLogs(Pageable pageable) {
        log.debug("Retrieving API call logs with pagination: {}", pageable);
        
        return logRepository.findAll(pageable)
                .map(log -> new ApiCallLogDto(
                        log.getId(),
                        log.getTimestamp(),
                        log.getEndpoint(),
                        log.getParametersJson(),
                        log.getResponseJson(),
                        log.getStatus().name()
                ));
    }
} 