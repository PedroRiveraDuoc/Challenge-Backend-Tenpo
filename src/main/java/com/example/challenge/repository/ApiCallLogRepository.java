package com.example.challenge.repository;

import com.example.challenge.model.ApiCallLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repository interface for API call logs.
 * Provides methods for persisting and retrieving log entries.
 */
@Repository
public interface ApiCallLogRepository extends JpaRepository<ApiCallLog, UUID> {
    
    /**
     * Retrieves a paginated list of API call logs.
     *
     * @param pageable pagination information
     * @return page of API call logs
     */
    Page<ApiCallLog> findAll(Pageable pageable);
} 