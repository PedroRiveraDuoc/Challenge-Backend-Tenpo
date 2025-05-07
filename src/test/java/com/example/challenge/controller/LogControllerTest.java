package com.example.challenge.controller;

import com.example.challenge.dto.ApiCallLogDto;
import com.example.challenge.service.LogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LogController.class)
class LogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LogService logService;

    @Test
    void getLogs_ShouldReturnPaginatedLogs() throws Exception {
        // Arrange
        UUID id = UUID.randomUUID();
        LocalDateTime timestamp = LocalDateTime.now();
        ApiCallLogDto log = new ApiCallLogDto(
                id,
                timestamp,
                "/test",
                "{\"param\":\"value\"}",
                "{\"result\":\"success\"}",
                "SUCCESS"
        );

        Page<ApiCallLogDto> logPage = new PageImpl<>(List.of(log));
        when(logService.getLogs(any(PageRequest.class))).thenReturn(logPage);

        // Act & Assert
        mockMvc.perform(get("/logs")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(id.toString()))
                .andExpect(jsonPath("$.content[0].endpoint").value("/test"))
                .andExpect(jsonPath("$.content[0].parametersJson").value("{\"param\":\"value\"}"))
                .andExpect(jsonPath("$.content[0].responseJson").value("{\"result\":\"success\"}"))
                .andExpect(jsonPath("$.content[0].status").value("SUCCESS"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void getLogs_ShouldUseDefaultPagination_WhenNoParamsProvided() throws Exception {
        // Arrange
        Page<ApiCallLogDto> emptyPage = new PageImpl<>(List.of());
        when(logService.getLogs(any(PageRequest.class))).thenReturn(emptyPage);

        // Act & Assert
        mockMvc.perform(get("/logs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(0));
    }
} 