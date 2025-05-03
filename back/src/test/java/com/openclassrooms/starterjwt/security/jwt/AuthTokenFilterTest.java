package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AuthTokenFilterTest {

    @InjectMocks
    private AuthTokenFilter authTokenFilter;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext(); // Clear security context before each test
    }

    @Test
    public void testDoFilterInternal_withValidJwt() throws ServletException, IOException {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer valid_test_token");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = new MockFilterChain();

        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "test@example.com", "John", "Doe", false, "password");
        
        // Configure mocks
        when(jwtUtils.validateJwtToken("valid_test_token")).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken("valid_test_token")).thenReturn("test@example.com");
        when(userDetailsService.loadUserByUsername("test@example.com")).thenReturn(userDetails);

        // When
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Then
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertEquals(userDetails, authentication.getPrincipal());
        
        verify(jwtUtils).validateJwtToken("valid_test_token");
        verify(jwtUtils).getUserNameFromJwtToken("valid_test_token");
        verify(userDetailsService).loadUserByUsername("test@example.com");
    }

    @Test
    public void testDoFilterInternal_withInvalidJwt() throws ServletException, IOException {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer invalid_test_token");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = new MockFilterChain();

        // Configure mocks
        when(jwtUtils.validateJwtToken("invalid_test_token")).thenReturn(false);

        // When
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Then
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication); // Authentication should not be set

        verify(jwtUtils).validateJwtToken("invalid_test_token");
        verify(jwtUtils, never()).getUserNameFromJwtToken(anyString());
        verify(userDetailsService, never()).loadUserByUsername(anyString());
    }

    @Test
    public void testDoFilterInternal_withNoToken() throws ServletException, IOException {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = new MockFilterChain();

        // When
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Then
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication); // Authentication should not be set

        verify(jwtUtils, never()).validateJwtToken(anyString());
        verify(jwtUtils, never()).getUserNameFromJwtToken(anyString());
        verify(userDetailsService, never()).loadUserByUsername(anyString());
    }

    @Test
    public void testDoFilterInternal_withMalformedTokenHeader() throws ServletException, IOException {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "NotBearer invalid_format");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = new MockFilterChain();

        // When
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Then
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication); // Authentication should not be set

        verify(jwtUtils, never()).validateJwtToken(anyString());
        verify(jwtUtils, never()).getUserNameFromJwtToken(anyString());
        verify(userDetailsService, never()).loadUserByUsername(anyString());
    }

    @Test
    public void testDoFilterInternal_withException() throws ServletException, IOException {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token_causing_exception");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = new MockFilterChain();

        // Configure mock to throw exception
        when(jwtUtils.validateJwtToken("token_causing_exception")).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken("token_causing_exception")).thenThrow(new RuntimeException("Test exception"));

        // When
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Then
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication); // Authentication should not be set due to exception

        verify(jwtUtils).validateJwtToken("token_causing_exception");
        verify(jwtUtils).getUserNameFromJwtToken("token_causing_exception");
        verify(userDetailsService, never()).loadUserByUsername(anyString());
    }

    @Test
    public void testParseJwt_validAuthorizationHeader() throws Exception {
        // Given
        HttpServletRequest request = new MockHttpServletRequest();
        ((MockHttpServletRequest) request).addHeader("Authorization", "Bearer test_token");

        // When
        String jwt = ReflectionTestUtils.invokeMethod(authTokenFilter, "parseJwt", request);

        // Then
        assertEquals("test_token", jwt);
    }

    @Test
    public void testParseJwt_noAuthorizationHeader() throws Exception {
        // Given
        HttpServletRequest request = new MockHttpServletRequest();

        // When
        String jwt = ReflectionTestUtils.invokeMethod(authTokenFilter, "parseJwt", request);

        // Then
        assertNull(jwt);
    }

    @Test
    public void testParseJwt_emptyAuthorizationHeader() throws Exception {
        // Given
        HttpServletRequest request = new MockHttpServletRequest();
        ((MockHttpServletRequest) request).addHeader("Authorization", "");

        // When
        String jwt = ReflectionTestUtils.invokeMethod(authTokenFilter, "parseJwt", request);

        // Then
        assertNull(jwt);
    }

    @Test
    public void testParseJwt_nonBearerAuthorizationHeader() throws Exception {
        // Given
        HttpServletRequest request = new MockHttpServletRequest();
        ((MockHttpServletRequest) request).addHeader("Authorization", "Basic dXNlcjpwYXNzd29yZA==");

        // When
        String jwt = ReflectionTestUtils.invokeMethod(authTokenFilter, "parseJwt", request);

        // Then
        assertNull(jwt);
    }
}