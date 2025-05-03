package com.openclassrooms.starterjwt.security.jwt;

import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilsTest {
    private JwtUtils jwtUtils;
    private final String jwtSecret = "testSecretKey123456789012345678901234567890";
    private final int jwtExpirationMs = 10000; // 10 seconds

    @BeforeEach
    public void setUp() {
        jwtUtils = new JwtUtils();
        org.springframework.test.util.ReflectionTestUtils.setField(jwtUtils, "jwtSecret", jwtSecret);
        org.springframework.test.util.ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", jwtExpirationMs);
    }

    @Test
    public void testGenerateAndValidateJwtToken() {
        UserDetailsImpl userDetails = new UserDetailsImpl(
            1L,
            "user@email.com",
            "John",
            "Doe",
            false,
            "password"
        );
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, null);
        String token = jwtUtils.generateJwtToken(authentication);
        assertNotNull(token);
        assertTrue(jwtUtils.validateJwtToken(token));
        assertEquals("user@email.com", jwtUtils.getUserNameFromJwtToken(token));
    }

    @Test
    public void testValidateJwtToken_invalidToken() {
        String invalidToken = "invalid.token.value";
        assertFalse(jwtUtils.validateJwtToken(invalidToken));
    }

    @Test
    public void testValidateJwtToken_expiredToken() throws InterruptedException {
        // Create a token with a very short expiration
        org.springframework.test.util.ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 1);
        UserDetailsImpl userDetails = new UserDetailsImpl(
            1L,
            "expired@email.com",
            "John",
            "Doe",
            false,
            "password"
        );
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, null);
        String token = jwtUtils.generateJwtToken(authentication);
        Thread.sleep(5); // Wait for token to expire
        assertFalse(jwtUtils.validateJwtToken(token));
    }

    @Test
    public void testGetUserNameFromJwtToken() {
        UserDetailsImpl userDetails = new UserDetailsImpl(
            1L,
            "user@email.com",
            "John",
            "Doe",
            false,
            "password"
        );
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, null);
        String token = jwtUtils.generateJwtToken(authentication);
        String username = jwtUtils.getUserNameFromJwtToken(token);
        assertEquals("user@email.com", username);
    }

    @Test
    public void testGetUserNameFromJwtToken_invalidToken() {
        String invalidToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyQGVtYWlsLmNvbSIsImlhdCI6MTcxMzY5MjU3OSwiZXhwIjoxNzEzNjkyNTg5fQ.INVALID";
        assertThrows(Exception.class, () -> jwtUtils.getUserNameFromJwtToken(invalidToken));
    }

    @Test
    public void testValidateJwtToken_malformedToken() {
        String malformedToken = "malformed.token";
        assertFalse(jwtUtils.validateJwtToken(malformedToken));
    }

    @Test
    public void testValidateJwtToken_nullToken() {
        String nullToken = null;
        assertFalse(jwtUtils.validateJwtToken(nullToken));
    }

    @Test
    public void testValidateJwtToken_emptyToken() {
        String emptyToken = "";
        assertFalse(jwtUtils.validateJwtToken(emptyToken));
    }
}
