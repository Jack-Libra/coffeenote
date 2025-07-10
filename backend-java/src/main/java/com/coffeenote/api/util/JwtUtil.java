package com.coffeenote.api.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JWT (JSON Web Token) 工具類
 * 
 * 這個工具類負責 JWT Token 的生成、解析和驗證。
 * 用於實現無狀態的使用者認證機制。
 * 
 * JWT Token 包含使用者 ID、過期時間等資訊，
 * 客戶端在每次 API 請求時都需要在 Header 中攜帶有效的 Token。
 * 
 * @author Coffee Journal Team
 * @version 1.0
 */
@Component
public class JwtUtil {
    
    /**
     * JWT 簽名密鑰
     * 從 application.properties 中讀取，如果未設定則使用預設值
     */
    @Value("${jwt.secret:mySecretKey12345678901234567890123456789012345678901234567890}")
    private String secret;
    
    /**
     * JWT Token 有效期（毫秒）
     * 預設為 24 小時
     */
    @Value("${jwt.expiration:86400000}")
    private Long expiration;
    
    /**
     * 獲取簽名密鑰
     * 
     * @return SecretKey 物件
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
    
    /**
     * 從 Token 中提取使用者名稱（Subject）
     * 
     * @param token JWT Token
     * @return 使用者名稱
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }
    
    /**
     * 從 Token 中提取使用者 ID
     * 
     * @param token JWT Token
     * @return 使用者 ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.get("userId", Long.class);
    }
    
    /**
     * 從 Token 中提取過期時間
     * 
     * @param token JWT Token
     * @return 過期時間
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }
    
    /**
     * 從 Token 中提取指定的聲明（Claim）
     * 
     * @param token JWT Token
     * @param claimsResolver 聲明解析器
     * @param <T> 返回類型
     * @return 聲明值
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    
    /**
     * 從 Token 中提取所有聲明
     * 
     * @param token JWT Token
     * @return Claims 物件
     * @throws JwtException 如果 Token 無效或過期
     */
    private Claims getAllClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new JwtException("JWT Token 已過期");
        } catch (UnsupportedJwtException e) {
            throw new JwtException("不支援的 JWT Token");
        } catch (MalformedJwtException e) {
            throw new JwtException("JWT Token 格式錯誤");
        } catch (IllegalArgumentException e) {
            throw new JwtException("JWT Token 為空");
        } catch (Exception e) {
            throw new JwtException("JWT Token 驗證失敗");
        }
    }
    
    /**
     * 檢查 Token 是否已過期
     * 
     * @param token JWT Token
     * @return 如果已過期則返回 true，否則返回 false
     */
    public Boolean isTokenExpired(String token) {
        try {
            final Date expiration = getExpirationDateFromToken(token);
            return expiration.before(new Date());
        } catch (Exception e) {
            return true; // 如果無法解析過期時間，則認為已過期
        }
    }
    
    /**
     * 為指定使用者生成 JWT Token
     * 
     * @param userId 使用者 ID
     * @param username 使用者名稱
     * @return JWT Token 字串
     */
    public String generateToken(Long userId, String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        return createToken(claims, username);
    }
    
    /**
     * 創建 JWT Token
     * 
     * @param claims 自訂聲明
     * @param subject 主題（通常是使用者名稱）
     * @return JWT Token 字串
     */
    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);
        
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }
    
    /**
     * 驗證 Token 是否有效
     * 
     * @param token JWT Token
     * @param username 使用者名稱
     * @return 如果 Token 有效且未過期則返回 true，否則返回 false
     */
    public Boolean validateToken(String token, String username) {
        try {
            final String tokenUsername = getUsernameFromToken(token);
            return (username.equals(tokenUsername) && !isTokenExpired(token));
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 驗證 Token 是否有效（僅檢查格式和過期時間）
     * 
     * @param token JWT Token
     * @return 如果 Token 有效且未過期則返回 true，否則返回 false
     */
    public Boolean validateToken(String token) {
        try {
            getAllClaimsFromToken(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 刷新 Token
     * 生成一個新的 Token，保持相同的使用者資訊但更新過期時間
     * 
     * @param token 舊的 JWT Token
     * @return 新的 JWT Token
     * @throws JwtException 如果舊 Token 無效
     */
    public String refreshToken(String token) {
        try {
            final Claims claims = getAllClaimsFromToken(token);
            final String username = claims.getSubject();
            final Long userId = claims.get("userId", Long.class);
            
            return generateToken(userId, username);
        } catch (Exception e) {
            throw new JwtException("無法刷新 Token: " + e.getMessage());
        }
    }
    
    /**
     * 從 Authorization Header 中提取 Token
     * 
     * @param authorizationHeader Authorization Header 的值
     * @return JWT Token，如果 Header 格式不正確則返回 null
     */
    public String extractTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
    
    /**
     * 獲取 Token 的剩餘有效時間（秒）
     * 
     * @param token JWT Token
     * @return 剩餘有效時間（秒），如果 Token 無效則返回 0
     */
    public long getTokenRemainingTime(String token) {
        try {
            Date expiration = getExpirationDateFromToken(token);
            Date now = new Date();
            long remainingTime = expiration.getTime() - now.getTime();
            return Math.max(0, remainingTime / 1000); // 轉換為秒
        } catch (Exception e) {
            return 0;
        }
    }
}
