package com.coffeenote.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

/**
 * 健康檢查控制器
 * 
 * 提供應用程式健康狀態檢查的端點，用於監控和負載均衡器。
 * 
 * @author Coffee Journal Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api")
public class HealthController {
    
    @Autowired
    private DataSource dataSource;
    
    /**
     * 基本健康檢查
     * 
     * GET /api/health
     * 
     * @return 健康狀態
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        
        try {
            // 檢查應用程式狀態
            health.put("status", "UP");
            health.put("timestamp", System.currentTimeMillis());
            health.put("service", "coffeenote-api");
            health.put("version", "1.0.0");
            
            // 檢查資料庫連接
            Map<String, Object> database = new HashMap<>();
            try (Connection connection = dataSource.getConnection()) {
                database.put("status", "UP");
                database.put("database", connection.getMetaData().getDatabaseProductName());
            } catch (Exception e) {
                database.put("status", "DOWN");
                database.put("error", e.getMessage());
            }
            health.put("database", database);
            
            // 檢查記憶體使用情況
            Runtime runtime = Runtime.getRuntime();
            Map<String, Object> memory = new HashMap<>();
            memory.put("total", runtime.totalMemory());
            memory.put("free", runtime.freeMemory());
            memory.put("used", runtime.totalMemory() - runtime.freeMemory());
            memory.put("max", runtime.maxMemory());
            health.put("memory", memory);
            
            return ResponseEntity.ok(health);
            
        } catch (Exception e) {
            health.put("status", "DOWN");
            health.put("error", e.getMessage());
            return ResponseEntity.status(503).body(health);
        }
    }
    
    /**
     * 簡單的存活檢查
     * 
     * GET /api/ping
     * 
     * @return pong 響應
     */
    @GetMapping("/ping")
    public ResponseEntity<Map<String, String>> ping() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "pong");
        response.put("timestamp", String.valueOf(System.currentTimeMillis()));
        return ResponseEntity.ok(response);
    }
}
