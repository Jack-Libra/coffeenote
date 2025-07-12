package com.coffeenote.api.filter;

import com.coffeenote.api.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * JWT 認證過濾器
 * 
 * 這個過濾器會攔截所有的 HTTP 請求，檢查請求頭中的 JWT Token。
 * 如果 Token 有效，則設置 Spring Security 的認證上下文。
 * 
 * 工作流程：
 * 1. 從請求頭中提取 JWT Token
 * 2. 驗證 Token 的有效性
 * 3. 從 Token 中提取用戶信息
 * 4. 設置 Spring Security 認證上下文
 * 
 * @author Coffee Journal Team
 * @version 1.0
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 執行過濾邏輯
     * 
     * @param request HTTP 請求
     * @param response HTTP 響應
     * @param filterChain 過濾器鏈
     * @throws ServletException Servlet 異常
     * @throws IOException IO 異常
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        // 從請求頭中獲取 Authorization 頭
        String authHeader = request.getHeader("Authorization");
        
        // 檢查是否存在 Bearer Token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // 移除 "Bearer " 前綴
            
            try {
                // 驗證 Token 並提取用戶信息
                if (jwtUtil.validateToken(token)) {
                    Long userId = jwtUtil.getUserIdFromToken(token);
                    String username = jwtUtil.getUsernameFromToken(token);

                    // 創建用戶詳情對象
                    UserDetails userDetails = User.builder()
                            .username(username)
                            .password("") // JWT 認證不需要密碼
                            .authorities(new ArrayList<>()) // 可以根據需要添加權限
                            .build();

                    // 創建認證對象
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    // 設置認證詳情
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // 將用戶 ID 添加到請求屬性中，方便後續使用
                    request.setAttribute("userId", userId);
                    request.setAttribute("username", username);

                    // 設置 Spring Security 上下文
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (JwtException e) {
                // Token 無效，記錄日誌但不阻止請求繼續
                logger.warn("Invalid JWT token: " + e.getMessage());
            } catch (Exception e) {
                // 其他異常，記錄日誌
                logger.error("JWT authentication error: " + e.getMessage());
            }
        }
        
        // 繼續過濾器鏈
        filterChain.doFilter(request, response);
    }
    
    /**
     * 判斷是否應該跳過此過濾器
     * 
     * @param request HTTP 請求
     * @return true 如果應該跳過，false 否則
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        
        // 跳過健康檢查和公開端點
        return path.equals("/api/health") || 
               path.equals("/api/ping") ||
               path.startsWith("/h2-console") ||
               path.startsWith("/actuator");
    }
}
