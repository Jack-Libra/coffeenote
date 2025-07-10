package com.coffeenote.api.controller;

import com.coffeenote.api.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 認證控制器
 * 
 * 處理使用者登入、登出、Token 刷新等認證相關的 API 請求。
 * 
 * @author Coffee Journal Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    /**
     * 使用者登入
     * 
     * POST /api/auth/login
     * Content-Type: application/json
     * 
     * 請求體：
     * {
     *   "username": "使用者名稱",
     *   "password": "密碼"
     * }
     * 
     * @param loginRequest 登入請求
     * @return JWT Token 和使用者資訊
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // 驗證使用者憑證
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(), 
                    loginRequest.getPassword()
                )
            );
            
            // 獲取認證成功的使用者詳細資訊
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            
            // 生成 JWT Token
            // 注意：這裡使用使用者名稱作為 userId，實際應用中應該使用真實的使用者 ID
            Long userId = getUserIdByUsername(userDetails.getUsername());
            String token = jwtUtil.generateToken(userId, userDetails.getUsername());
            
            // 構建響應
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("type", "Bearer");
            response.put("username", userDetails.getUsername());
            response.put("userId", userId);
            response.put("expiresIn", 86400); // 24 小時（秒）
            
            return ResponseEntity.ok(response);
            
        } catch (BadCredentialsException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "認證失敗");
            error.put("message", "使用者名稱或密碼錯誤");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "登入失敗");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * 刷新 Token
     * 
     * POST /api/auth/refresh
     * Authorization: Bearer <token>
     * 
     * @param authorization JWT Token
     * @return 新的 JWT Token
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String authorization) {
        try {
            // 提取 Token
            String token = jwtUtil.extractTokenFromHeader(authorization);
            if (token == null) {
                throw new Exception("無效的 Authorization Header");
            }
            
            // 刷新 Token
            String newToken = jwtUtil.refreshToken(token);
            
            // 獲取使用者資訊
            String username = jwtUtil.getUsernameFromToken(newToken);
            Long userId = jwtUtil.getUserIdFromToken(newToken);
            
            // 構建響應
            Map<String, Object> response = new HashMap<>();
            response.put("token", newToken);
            response.put("type", "Bearer");
            response.put("username", username);
            response.put("userId", userId);
            response.put("expiresIn", 86400); // 24 小時（秒）
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Token 刷新失敗");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }
    
    /**
     * 驗證 Token 有效性
     * 
     * GET /api/auth/validate
     * Authorization: Bearer <token>
     * 
     * @param authorization JWT Token
     * @return Token 驗證結果
     */
    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authorization) {
        try {
            // 提取 Token
            String token = jwtUtil.extractTokenFromHeader(authorization);
            if (token == null) {
                throw new Exception("無效的 Authorization Header");
            }
            
            // 驗證 Token
            boolean isValid = jwtUtil.validateToken(token);
            
            if (isValid) {
                // 獲取使用者資訊
                String username = jwtUtil.getUsernameFromToken(token);
                Long userId = jwtUtil.getUserIdFromToken(token);
                long remainingTime = jwtUtil.getTokenRemainingTime(token);
                
                Map<String, Object> response = new HashMap<>();
                response.put("valid", true);
                response.put("username", username);
                response.put("userId", userId);
                response.put("remainingTime", remainingTime);
                
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("valid", false);
                response.put("message", "Token 已過期或無效");
                
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Token 驗證失敗");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }
    
    /**
     * 使用者登出
     * 
     * POST /api/auth/logout
     * Authorization: Bearer <token>
     * 
     * 注意：由於 JWT 是無狀態的，伺服器端無法直接使 Token 失效。
     * 在實際應用中，可以維護一個黑名單來記錄已登出的 Token。
     * 
     * @param authorization JWT Token
     * @return 登出結果
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authorization) {
        try {
            // 提取 Token
            String token = jwtUtil.extractTokenFromHeader(authorization);
            if (token == null) {
                throw new Exception("無效的 Authorization Header");
            }
            
            // 在實際應用中，這裡應該將 Token 添加到黑名單
            // 目前只是返回成功響應
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "登出成功");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "登出失敗");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
    
    /**
     * 根據使用者名稱獲取使用者 ID
     * 
     * 這是一個簡化的實現，實際應用中應該從資料庫查詢真實的使用者 ID
     * 
     * @param username 使用者名稱
     * @return 使用者 ID
     */
    private Long getUserIdByUsername(String username) {
        // 簡化實現：使用使用者名稱的雜湊值作為 ID
        // 實際應用中應該從資料庫查詢
        switch (username) {
            case "testuser":
                return 1L;
            case "admin":
                return 2L;
            default:
                return (long) username.hashCode();
        }
    }
    
    /**
     * 登入請求 DTO
     */
    public static class LoginRequest {
        private String username;
        private String password;
        
        // Getters and Setters
        public String getUsername() {
            return username;
        }
        
        public void setUsername(String username) {
            this.username = username;
        }
        
        public String getPassword() {
            return password;
        }
        
        public void setPassword(String password) {
            this.password = password;
        }
    }
}
