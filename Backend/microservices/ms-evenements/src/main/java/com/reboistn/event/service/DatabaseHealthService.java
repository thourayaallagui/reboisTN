package com.reboistn.event.service;

import com.reboistn.event.dto.DatabaseHealthResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class DatabaseHealthService {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseHealthService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public DatabaseHealthResponse checkDatabaseConnection() {
        Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
        return new DatabaseHealthResponse("H2", "CONNECTED", result);
    }
}
