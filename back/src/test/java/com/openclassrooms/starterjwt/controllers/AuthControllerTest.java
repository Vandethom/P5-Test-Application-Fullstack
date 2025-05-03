package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.services.UserService;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthControllerTest {
    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    UserService userService;
    @Mock
    PasswordEncoder encoder;
    @Mock
    JwtUtils jwtUtils;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    AuthController authController;

    public AuthControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoginSuccess() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("user@email.com");
        loginRequest.setPassword("password");
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("token");
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn(1L);
        when(userDetails.getUsername()).thenReturn("user@email.com");
        when(userDetails.getFirstName()).thenReturn("John");
        when(userDetails.getLastName()).thenReturn("Doe");
        User user = new User();
        user.setEmail("user@email.com");
        user.setAdmin(false);
        when(userRepository.findByEmail("user@email.com")).thenReturn(java.util.Optional.of(user));
        ResponseEntity<?> response = authController.authenticateUser(loginRequest);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void testLoginBadCredentials() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("user@email.com");
        loginRequest.setPassword("wrong");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new RuntimeException("Bad credentials"));
        assertThrows(RuntimeException.class, () -> authController.authenticateUser(loginRequest));
    }

    @Test
    public void testLoginMissingField() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(null);
        loginRequest.setPassword(null);
        // Should fail validation or throw
        assertThrows(Exception.class, () -> authController.authenticateUser(loginRequest));
    }

    @Test
    public void testRegisterSuccess() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("user@email.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("password");
        when(userRepository.existsByEmail("user@email.com")).thenReturn(false);
        when(encoder.encode(anyString())).thenReturn("encodedPassword");
        // Return the same user passed to save to avoid NPE on @NonNull fields
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        ResponseEntity<?> response = authController.registerUser(signupRequest);
        assertNotNull(response);
    }

    @Test
    public void testRegisterMissingField() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail(null);
        signupRequest.setFirstName(null);
        signupRequest.setLastName(null);
        signupRequest.setPassword(null);
        assertThrows(Exception.class, () -> authController.registerUser(signupRequest));
    }

    @Test
    public void testRegisterEmailExists() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("user@email.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("password");
        when(userRepository.existsByEmail("user@email.com")).thenReturn(true);
        ResponseEntity<?> response = authController.registerUser(signupRequest);
        assertEquals(400, response.getStatusCodeValue());
    }
}
