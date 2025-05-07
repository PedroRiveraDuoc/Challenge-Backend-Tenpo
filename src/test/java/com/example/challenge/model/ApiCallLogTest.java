package com.example.challenge.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ApiCallLogTest {

    @Test
    void testBuilderAndGetters() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        ApiCallLog log = ApiCallLog.builder()
                .id(id)
                .timestamp(now)
                .endpoint("/api/test")
                .parametersJson("{\"param\":\"value\"}")
                .responseJson("{\"result\":\"ok\"}")
                .status(ApiCallLog.ApiCallStatus.SUCCESS)
                .build();

        assertEquals(id, log.getId());
        assertEquals(now, log.getTimestamp());
        assertEquals("/api/test", log.getEndpoint());
        assertEquals("{\"param\":\"value\"}", log.getParametersJson());
        assertEquals("{\"result\":\"ok\"}", log.getResponseJson());
        assertEquals(ApiCallLog.ApiCallStatus.SUCCESS, log.getStatus());
    }

    @Test
    void testSettersAndEquals() {
        ApiCallLog log1 = new ApiCallLog();
        log1.setEndpoint("/endpoint");
        log1.setStatus(ApiCallLog.ApiCallStatus.ERROR);

        ApiCallLog log2 = new ApiCallLog();
        log2.setEndpoint("/endpoint");
        log2.setStatus(ApiCallLog.ApiCallStatus.ERROR);

        assertEquals(log1, log2); // Probando equals y hashCode
        assertEquals(log1.hashCode(), log2.hashCode());
    }

    @Test
    void testToStringCoverage() {
        ApiCallLog log = new ApiCallLog();
        log.setEndpoint("/endpoint");
        log.setStatus(ApiCallLog.ApiCallStatus.SUCCESS);

        String stringRepresentation = log.toString();
        assertNotNull(stringRepresentation);
    }
}
