package com.openclassrooms.starterjwt.security.jwt;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class AuthEntryPointJwtTest {
    
    @Test
    public void testCommenceReturns401WithCorrectBody() throws Exception {
        // Given
        AuthEntryPointJwt authEntryPoint = new AuthEntryPointJwt();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServletPath("/api/test");
        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthenticationException authException = new BadCredentialsException("Invalid credentials");

        // When
        authEntryPoint.commence(request, response, authException);

        // Then
        assertEquals(401, response.getStatus());
        assertNotNull(response.getContentType());
        assertTrue(response.getContentType() != null && response.getContentType().contains("application/json"));
        String responseBody = response.getContentAsString();
        assertTrue(responseBody.contains("\"status\":401"));
        assertTrue(responseBody.contains("\"error\":\"Unauthorized\""));
        assertTrue(responseBody.contains("\"message\":\"Invalid credentials\""));
        assertTrue(responseBody.contains("\"path\":\"/api/test\""));
    }
    
    @Test
    public void testCommenceWithInsufficientAuthentication() throws IOException, ServletException {
        // Given
        AuthEntryPointJwt authEntryPoint = new AuthEntryPointJwt();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServletPath("/api/secured");
        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthenticationException authException = new InsufficientAuthenticationException("Full authentication is required");

        // When
        authEntryPoint.commence(request, response, authException);

        // Then
        assertEquals(401, response.getStatus());
        assertNotNull(response.getContentType());
        assertTrue(response.getContentType() != null && response.getContentType().contains("application/json"));
        String responseBody = response.getContentAsString();
        assertTrue(responseBody.contains("\"status\":401"));
        assertTrue(responseBody.contains("\"error\":\"Unauthorized\""));
        assertTrue(responseBody.contains("\"message\":\"Full authentication is required\""));
        assertTrue(responseBody.contains("\"path\":\"/api/secured\""));
    }
    
    @Test
    public void testCommenceWithUserNotFound() throws IOException, ServletException {
        // Given
        AuthEntryPointJwt authEntryPoint = new AuthEntryPointJwt();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServletPath("/api/login");
        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthenticationException authException = new UsernameNotFoundException("User not found");

        // When
        authEntryPoint.commence(request, response, authException);

        // Then
        assertEquals(401, response.getStatus());
        assertNotNull(response.getContentType());
        assertTrue(response.getContentType() != null && response.getContentType().contains("application/json"));
        String responseBody = response.getContentAsString();
        assertTrue(responseBody.contains("\"message\":\"User not found\""));
    }
    
    @Test
    public void testCommenceWithAccountExpired() throws IOException, ServletException {
        // Given
        AuthEntryPointJwt authEntryPoint = new AuthEntryPointJwt();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServletPath("/api/resource");
        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthenticationException authException = new AccountExpiredException("Account has expired");

        // When
        authEntryPoint.commence(request, response, authException);

        // Then
        assertEquals(401, response.getStatus());
        assertNotNull(response.getContentType());
        assertTrue(response.getContentType() != null && response.getContentType().contains("application/json"));
        String responseBody = response.getContentAsString();
        assertTrue(responseBody.contains("\"message\":\"Account has expired\""));
    }
    
    @Test
    public void testCommenceWithCredentialsExpired() throws IOException, ServletException {
        // Given
        AuthEntryPointJwt authEntryPoint = new AuthEntryPointJwt();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServletPath("/api/data");
        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthenticationException authException = new CredentialsExpiredException("Credentials have expired");

        // When
        authEntryPoint.commence(request, response, authException);

        // Then
        assertEquals(401, response.getStatus());
        assertNotNull(response.getContentType());
        assertTrue(response.getContentType() != null && response.getContentType().contains("application/json"));
        String responseBody = response.getContentAsString();
        assertTrue(responseBody.contains("\"message\":\"Credentials have expired\""));
    }
    
    @Test
    public void testCommenceWithNullServletPath() throws IOException, ServletException {
        // Given
        AuthEntryPointJwt authEntryPoint = new AuthEntryPointJwt();
        MockHttpServletRequest request = new MockHttpServletRequest();
        // Not setting servlet path, it will default to empty string
        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthenticationException authException = new BadCredentialsException("Authentication failed");

        // When
        authEntryPoint.commence(request, response, authException);

        // Then
        assertEquals(401, response.getStatus());
        assertNotNull(response.getContentType());
        assertTrue(response.getContentType() != null && response.getContentType().contains("application/json"));
        String responseBody = response.getContentAsString();
        assertTrue(responseBody.contains("\"message\":\"Authentication failed\""));
        assertTrue(responseBody.contains("\"path\":\"\""));  // Empty path instead of "null"
    }
}