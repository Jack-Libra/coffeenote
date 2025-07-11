package com.coffeenote.api.util;

import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JWT 工具類測試
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class JwtUtilTest {

    private JwtUtil jwtUtil;
    private final String testSecret = "mySecretKey12345678901234567890123456789012345678901234567890";
    private final Long testExpiration = 86400000L; // 24 hours

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        // 使用反射設定私有字段
        ReflectionTestUtils.setField(jwtUtil, "secret", testSecret);
        ReflectionTestUtils.setField(jwtUtil, "expiration", testExpiration);
    }

    @Test
    void testGenerateToken() {
        // Given
        Long userId = 1L;
        String username = "testuser@example.com";

        // When
        String token = jwtUtil.generateToken(userId, username);

        // Then
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.contains("."));
    }

    @Test
    void testGetUserIdFromToken() {
        // Given
        Long userId = 123L;
        String username = "testuser@example.com";
        String token = jwtUtil.generateToken(userId, username);

        // When
        Long extractedUserId = jwtUtil.getUserIdFromToken(token);

        // Then
        assertEquals(userId, extractedUserId);
    }

    @Test
    void testGetUsernameFromToken() {
        // Given
        Long userId = 1L;
        String username = "testuser@example.com";
        String token = jwtUtil.generateToken(userId, username);

        // When
        String extractedUsername = jwtUtil.getUsernameFromToken(token);

        // Then
        assertEquals(username, extractedUsername);
    }

    @Test
    void testValidateToken() {
        // Given
        Long userId = 1L;
        String username = "testuser@example.com";
        String token = jwtUtil.generateToken(userId, username);

        // When
        Boolean isValid = jwtUtil.validateToken(token);

        // Then
        assertTrue(isValid);
    }

    @Test
    void testValidateTokenWithUsername() {
        // Given
        Long userId = 1L;
        String username = "testuser@example.com";
        String token = jwtUtil.generateToken(userId, username);

        // When
        Boolean isValid = jwtUtil.validateToken(token, username);

        // Then
        assertTrue(isValid);
    }

    @Test
    void testValidateTokenWithWrongUsername() {
        // Given
        Long userId = 1L;
        String username = "testuser@example.com";
        String wrongUsername = "wronguser@example.com";
        String token = jwtUtil.generateToken(userId, username);

        // When
        Boolean isValid = jwtUtil.validateToken(token, wrongUsername);

        // Then
        assertFalse(isValid);
    }

    @Test
    void testInvalidToken() {
        // Given
        String invalidToken = "invalid.token.here";

        // When & Then
        assertThrows(JwtException.class, () -> {
            jwtUtil.getUserIdFromToken(invalidToken);
        });
    }

    @Test
    void testExtractTokenFromHeader() {
        // Given
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...";
        String authHeader = "Bearer " + token;

        // When
        String extractedToken = jwtUtil.extractTokenFromHeader(authHeader);

        // Then
        assertEquals(token, extractedToken);
    }

    @Test
    void testExtractTokenFromInvalidHeader() {
        // Given
        String invalidHeader = "InvalidHeader";

        // When
        String extractedToken = jwtUtil.extractTokenFromHeader(invalidHeader);

        // Then
        assertNull(extractedToken);
    }

    @Test
    void testGetTokenRemainingTime() {
        // Given
        Long userId = 1L;
        String username = "testuser@example.com";
        String token = jwtUtil.generateToken(userId, username);

        // When
        long remainingTime = jwtUtil.getTokenRemainingTime(token);

        // Then
        assertTrue(remainingTime > 0);
        assertTrue(remainingTime <= 86400); // Should be less than or equal to 24 hours
    }

    @Test
    void testRefreshToken() throws InterruptedException {
        // Given
        Long userId = 1L;
        String username = "testuser@example.com";
        String originalToken = jwtUtil.generateToken(userId, username);

        // Wait a moment to ensure different timestamps
        Thread.sleep(1000);

        // When
        String refreshedToken = jwtUtil.refreshToken(originalToken);

        // Then
        assertNotNull(refreshedToken);
        assertNotEquals(originalToken, refreshedToken);

        // Verify the refreshed token contains the same user info
        assertEquals(userId, jwtUtil.getUserIdFromToken(refreshedToken));
        assertEquals(username, jwtUtil.getUsernameFromToken(refreshedToken));
    }

    @Test
    void testTokenExpiration() {
        // Given - Create a token with very short expiration
        JwtUtil shortExpirationJwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(shortExpirationJwtUtil, "secret", testSecret);
        ReflectionTestUtils.setField(shortExpirationJwtUtil, "expiration", 1L); // 1 millisecond

        Long userId = 1L;
        String username = "testuser@example.com";
        String token = shortExpirationJwtUtil.generateToken(userId, username);

        // Wait for token to expire
        try {
            Thread.sleep(10); // Wait 10ms to ensure expiration
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // When & Then
        assertThrows(JwtException.class, () -> {
            shortExpirationJwtUtil.getUserIdFromToken(token);
        });
    }
}
