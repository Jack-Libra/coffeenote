package com.coffeenote.api.security;

import com.coffeenote.api.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * JWT 認證過濾器
 * 
 * 這個過濾器會攔截所有的 HTTP 請求，檢查請求頭中的 JWT Token，
 * 如果 Token 有效，則設定 Spring Security 的認證上下文。
 * 
 * 繼承 OncePerRequestFilter 確保每個請求只執行一次過濾。
 * 
 * @author Coffee Journal Team
 * @version 1.0
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private UserDetailsService userDetailsService;
    
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
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        // 獲取 Authorization Header
        final String authorizationHeader = request.getHeader("Authorization");
        
        String username = null;
        String jwt = null;
        
        // 檢查 Authorization Header 格式
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7); // 移除 "Bearer " 前綴
            
            try {
                // 從 JWT Token 中提取使用者名稱
                username = jwtUtil.getUsernameFromToken(jwt);
            } catch (Exception e) {
                // Token 無效，記錄錯誤但不中斷請求處理
                logger.warn("無法從 JWT Token 中提取使用者名稱: " + e.getMessage());
            }
        }
        
        // 如果提取到使用者名稱且當前沒有認證上下文
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            
            try {
                // 載入使用者詳細資訊
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                
                // 驗證 Token 是否有效
                if (jwtUtil.validateToken(jwt, userDetails.getUsername())) {
                    
                    // 創建認證 Token
                    UsernamePasswordAuthenticationToken authToken = 
                        new UsernamePasswordAuthenticationToken(
                            userDetails, 
                            null, 
                            userDetails.getAuthorities()
                        );
                    
                    // 設定認證詳細資訊
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    // 設定 Spring Security 認證上下文
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    
                    // 將使用者 ID 添加到請求屬性中，方便控制器使用
                    Long userId = jwtUtil.getUserIdFromToken(jwt);
                    request.setAttribute("userId", userId);
                }
                
            } catch (Exception e) {
                // 認證失敗，記錄錯誤但不中斷請求處理
                logger.warn("JWT 認證失敗: " + e.getMessage());
            }
        }
        
        // 繼續執行過濾器鏈
        filterChain.doFilter(request, response);
    }
    
    /**
     * 判斷是否應該跳過此過濾器
     * 對於某些不需要認證的端點（如登入、註冊），可以跳過 JWT 驗證
     * 
     * @param request HTTP 請求
     * @return 如果應該跳過則返回 true，否則返回 false
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        
        // 跳過認證的路徑
        return path.startsWith("/api/auth/") ||  // 認證相關端點
               path.startsWith("/api/public/") || // 公開端點
               path.equals("/api/health") ||      // 健康檢查端點
               path.startsWith("/swagger-") ||    // Swagger 文檔
               path.startsWith("/v3/api-docs");   // OpenAPI 文檔
    }
}
