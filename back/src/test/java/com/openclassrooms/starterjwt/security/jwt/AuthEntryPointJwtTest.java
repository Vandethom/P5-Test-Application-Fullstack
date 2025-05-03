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
        AuthEntryPointJwt authEntryPoint = new AuthEntryPointJwt();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServletPath("/api/test");
        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthenticationException authException = new BadCredentialsException("Invalid credentials");

        authEntryPoint.commence(request, response, authException);

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
        AuthEntryPointJwt authEntryPoint = new AuthEntryPointJwt();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServletPath("/api/secured");
        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthenticationException authException = new InsufficientAuthenticationException("Full authentication is required");

        authEntryPoint.commence(request, response, authException);

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
        AuthEntryPointJwt authEntryPoint = new AuthEntryPointJwt();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServletPath("/api/login");
        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthenticationException authException = new UsernameNotFoundException("User not found");

        authEntryPoint.commence(request, response, authException);

        assertEquals(401, response.getStatus());
        assertNotNull(response.getContentType());
        assertTrue(response.getContentType() != null && response.getContentType().contains("application/json"));
        String responseBody = response.getContentAsString();
        assertTrue(responseBody.contains("\"message\":\"User not found\""));
    }
    
    @Test
    public void testCommenceWithAccountExpired() throws IOException, ServletException {
        AuthEntryPointJwt authEntryPoint = new AuthEntryPointJwt();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServletPath("/api/resource");
        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthenticationException authException = new AccountExpiredException("Account has expired");

        authEntryPoint.commence(request, response, authException);

        assertEquals(401, response.getStatus());
        assertNotNull(response.getContentType());
        assertTrue(response.getContentType() != null && response.getContentType().contains("application/json"));
        String responseBody = response.getContentAsString();
        assertTrue(responseBody.contains("\"message\":\"Account has expired\""));
    }
    
    @Test
    public void testCommenceWithCredentialsExpired() throws IOException, ServletException {
        AuthEntryPointJwt authEntryPoint = new AuthEntryPointJwt();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServletPath("/api/data");
        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthenticationException authException = new CredentialsExpiredException("Credentials have expired");

        authEntryPoint.commence(request, response, authException);

        assertEquals(401, response.getStatus());
        assertNotNull(response.getContentType());
        assertTrue(response.getContentType() != null && response.getContentType().contains("application/json"));
        String responseBody = response.getContentAsString();
        assertTrue(responseBody.contains("\"message\":\"Credentials have expired\""));
    }
    
    @Test
    public void testCommenceWithNullServletPath() throws IOException, ServletException {
        AuthEntryPointJwt authEntryPoint = new AuthEntryPointJwt();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthenticationException authException = new BadCredentialsException("Authentication failed");

        authEntryPoint.commence(request, response, authException);

        assertEquals(401, response.getStatus());
        assertNotNull(response.getContentType());
        assertTrue(response.getContentType() != null && response.getContentType().contains("application/json"));
        String responseBody = response.getContentAsString();
        assertTrue(responseBody.contains("\"message\":\"Authentication failed\""));
        assertTrue(responseBody.contains("\"path\":\"\""));
    }
}