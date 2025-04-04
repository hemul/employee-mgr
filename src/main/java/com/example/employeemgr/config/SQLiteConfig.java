package com.example.employeemgr.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class SQLiteConfig {
    private static final Logger logger = LoggerFactory.getLogger(SQLiteConfig.class);

    @Value("${app.database.directory:./data}")
    private String databaseDirectory;

    @Value("${app.database.name:employee-mgr.db}")
    private String databaseName;

    @Bean
    public DataSource dataSource() {
        try {
            // Create database directory if it doesn't exist
            Path dbDir = Paths.get(databaseDirectory);
            if (!Files.exists(dbDir)) {
                Files.createDirectories(dbDir);
                logger.info("Created database directory: {}", dbDir);
            }

            // Set up database file path
            File dbFile = dbDir.resolve(databaseName).toFile();
            String dbUrl = "jdbc:sqlite:" + dbFile.getAbsolutePath();
            logger.info("Using database file: {}", dbFile.getAbsolutePath());

            // Configure data source
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName("org.sqlite.JDBC");
            dataSource.setUrl(dbUrl);
            
            return dataSource;
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize SQLite database", e);
        }
    }
} 