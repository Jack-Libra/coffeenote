package com.coffeenote.api.config;

import com.coffeenote.api.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Spring Security 安全配置類
 * 
 * 這個配置類定義了應用程式的安全策略，包括：
 * - JWT 認證機制
 * - CORS 跨域配置
 * - 端點存取權限
 * - 密碼編碼器
 * 
 * @author Coffee Journal Team
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    
    /**
     * 配置 HTTP 安全策略
     *
     * @param http HttpSecurity 物件
     * @param jwtAuthenticationFilter JWT 認證過濾器
     * @return SecurityFilterChain
     * @throws Exception 配置異常
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
            // 禁用 CSRF，因為使用 JWT Token
            .csrf(csrf -> csrf.disable())
            
            // 配置 CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // 配置 Session 管理策略為無狀態
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // 配置端點存取權限
            .authorizeHttpRequests(authz -> authz
                // 允許所有人存取的端點
                .requestMatchers("/api/auth/**").permitAll()           // 認證相關
                .requestMatchers("/api/public/**").permitAll()         // 公開 API
                .requestMatchers("/api/health").permitAll()            // 健康檢查
                .requestMatchers("/swagger-ui/**").permitAll()         // Swagger UI
                .requestMatchers("/v3/api-docs/**").permitAll()        // OpenAPI 文檔
                .requestMatchers("/swagger-resources/**").permitAll()  // Swagger 資源
                .requestMatchers("/webjars/**").permitAll()            // WebJars
                
                // 其他所有端點都需要認證
                .anyRequest().authenticated()
            )
            
            // 添加 JWT 認證過濾器
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
    
    /**
     * 配置 CORS（跨域資源共享）
     * 
     * @return CorsConfigurationSource
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // 允許的來源（生產環境應該限制為特定域名）
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        
        // 允許的 HTTP 方法
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        
        // 允許的請求頭
        configuration.setAllowedHeaders(Arrays.asList("*"));
        
        // 允許發送認證資訊（如 Cookies）
        configuration.setAllowCredentials(true);
        
        // 預檢請求的有效期
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
    
    /**
     * 密碼編碼器
     * 使用 BCrypt 演算法進行密碼雜湊
     * 
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    /**
     * 認證管理器
     * 
     * @param config AuthenticationConfiguration
     * @return AuthenticationManager
     * @throws Exception 配置異常
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    
    /**
     * 使用者詳細資訊服務
     * 
     * 這裡使用記憶體中的使用者管理器作為示例。
     * 在實際應用中，應該實現自訂的 UserDetailsService，
     * 從資料庫中載入使用者資訊。
     * 
     * @return UserDetailsService
     */
    @Bean
    public UserDetailsService userDetailsService() {
        // 創建測試使用者
        UserDetails user = User.builder()
                .username("testuser")
                .password(passwordEncoder().encode("password"))
                .roles("USER")
                .build();
        
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin"))
                .roles("USER", "ADMIN")
                .build();
        
        return new InMemoryUserDetailsManager(user, admin);
    }
}
