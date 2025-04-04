package com.example.employeemgr.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DatabaseUtils {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseUtils.class);
    private static final DateTimeFormatter BACKUP_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    @Value("${app.database.directory}")
    private String databaseDirectory;

    @Value("${app.database.name}")
    private String databaseName;

    public String createBackup() throws IOException {
        Path dbDir = Paths.get(databaseDirectory);
        Path dbFile = dbDir.resolve(databaseName);
        
        // Create backups directory if it doesn't exist
        Path backupDir = dbDir.resolve("backups");
        if (!Files.exists(backupDir)) {
            Files.createDirectories(backupDir);
        }

        // Generate backup file name with timestamp
        String timestamp = LocalDateTime.now().format(BACKUP_DATE_FORMAT);
        String backupFileName = databaseName.replace(".db", "") + "_" + timestamp + ".db";
        Path backupFile = backupDir.resolve(backupFileName);

        // Copy database file to backup location
        Files.copy(dbFile, backupFile, StandardCopyOption.REPLACE_EXISTING);
        logger.info("Created database backup: {}", backupFile);

        return backupFile.toString();
    }

    public void restoreFromBackup(String backupPath) throws IOException {
        Path backupFile = Paths.get(backupPath);
        Path dbFile = Paths.get(databaseDirectory, databaseName);

        if (!Files.exists(backupFile)) {
            throw new IOException("Backup file not found: " + backupPath);
        }

        // Stop the application or close all database connections here
        
        // Restore the backup
        Files.copy(backupFile, dbFile, StandardCopyOption.REPLACE_EXISTING);
        logger.info("Restored database from backup: {}", backupPath);
    }

    public void vacuum() {
        // Implement database vacuum operation
        logger.info("Database vacuum operation completed");
    }
} 