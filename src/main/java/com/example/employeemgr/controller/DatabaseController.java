package com.example.employeemgr.controller;

import com.example.employeemgr.util.DatabaseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/database")
public class DatabaseController {

    @Autowired
    private DatabaseUtils databaseUtils;

    @PostMapping("/backup")
    public ResponseEntity<String> createBackup() {
        try {
            String backupPath = databaseUtils.createBackup();
            return ResponseEntity.ok("Backup created successfully: " + backupPath);
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body("Failed to create backup: " + e.getMessage());
        }
    }

    @PostMapping("/restore")
    public ResponseEntity<String> restoreBackup(@RequestParam String backupPath) {
        try {
            databaseUtils.restoreFromBackup(backupPath);
            return ResponseEntity.ok("Database restored successfully from: " + backupPath);
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body("Failed to restore backup: " + e.getMessage());
        }
    }

    @PostMapping("/vacuum")
    public ResponseEntity<String> vacuum() {
        databaseUtils.vacuum();
        return ResponseEntity.ok("Database vacuum completed successfully");
    }
} 