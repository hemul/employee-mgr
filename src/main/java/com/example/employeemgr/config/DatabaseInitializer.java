package com.example.employeemgr.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) {
        // Verify database connection and tables
        try {
            jdbcTemplate.queryForObject("SELECT COUNT(*) FROM employee", Integer.class);
        } catch (Exception e) {
            throw new RuntimeException("Database initialization failed", e);
        }
    }
} 