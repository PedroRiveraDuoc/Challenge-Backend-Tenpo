package com.example.challenge.service;

import com.example.challenge.dto.ApiCallLogDto;
import com.example.challenge.model.ApiCallLog;
import com.example.challenge.repository.ApiCallLogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LogServiceTest {

    @Mock
    private ApiCallLogRepository logRepository;

    @InjectMocks
    private LogService logService;

    @Captor
    private ArgumentCaptor<ApiCallLog> logCaptor;

    @Test
    void saveLog_ShouldSaveLogEntry() {
        // Arrange
        ApiCallLogDto logDto = new ApiCallLogDto(
                null,
                null,
                "/test",
                "{\"param\":\"value\"}",
                "{\"result\":\"success\"}",
                "SUCCESS"
        );

        // Act
        logService.saveLog(logDto);

        // Assert
        verify(logRepository).save(logCaptor.capture());
        ApiCallLog savedLog = logCaptor.getValue();
        assertEquals("/test", savedLog.getEndpoint());
        assertEquals("{\"param\":\"value\"}", savedLog.getParametersJson());
        assertEquals("{\"result\":\"success\"}", savedLog.getResponseJson());
        assertEquals(ApiCallLog.ApiCallStatus.SUCCESS, savedLog.getStatus());
    }

    @Test
    void getLogs_ShouldReturnPaginatedLogs() {
        // Arrange
        UUID id = UUID.randomUUID();
        ApiCallLog log = ApiCallLog.builder()
                .id(id)
                .endpoint("/test")
                .parametersJson("{\"param\":\"value\"}")
                .responseJson("{\"result\":\"success\"}")
                .status(ApiCallLog.ApiCallStatus.SUCCESS)
                .build();

        Page<ApiCallLog> logPage = new PageImpl<>(List.of(log));
        when(logRepository.findAll(any(Pageable.class))).thenReturn(logPage);

        // Act
        Page<ApiCallLogDto> result = logService.getLogs(PageRequest.of(0, 10));

        // Assert
        assertEquals(1, result.getTotalElements());
        ApiCallLogDto dto = result.getContent().get(0);
        assertEquals(id, dto.id());
        assertEquals("/test", dto.endpoint());
        assertEquals("{\"param\":\"value\"}", dto.parametersJson());
        assertEquals("{\"result\":\"success\"}", dto.responseJson());
        assertEquals("SUCCESS", dto.status());
    }
} 