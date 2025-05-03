package com.openclassrooms.starterjwt.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthenticationIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private final String TEST_EMAIL = "integration-test@example.com";
    private final String TEST_PASSWORD = "password123";

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
    }

    @Test
    public void testRegisterAndLoginUser() throws Exception {
        // Register a new user
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail(TEST_EMAIL);
        signupRequest.setFirstName("Integration");
        signupRequest.setLastName("Test");
        signupRequest.setPassword(TEST_PASSWORD);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isOk());

        // Verify user is in the database
        User savedUser = userRepository.findByEmail(TEST_EMAIL)
                .orElseThrow(() -> new AssertionError("User not saved to database"));
        assertEquals(TEST_EMAIL, savedUser.getEmail());
        assertEquals("Integration", savedUser.getFirstName());
        assertEquals("Test", savedUser.getLastName());
        assertTrue(passwordEncoder.matches(TEST_PASSWORD, savedUser.getPassword()));
        assertFalse(savedUser.isAdmin());

        // Login with the registered user
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(TEST_EMAIL);
        loginRequest.setPassword(TEST_PASSWORD);

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.type").value("Bearer"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.username").value(TEST_EMAIL))
                .andExpect(jsonPath("$.firstName").value("Integration"))
                .andExpect(jsonPath("$.lastName").value("Test"))
                .andReturn();
    }

    @Test
    public void testLoginWithInvalidCredentials() throws Exception {
        // Create a user for testing
        User user = new User();
        user.setEmail(TEST_EMAIL);
        user.setFirstName("Integration");
        user.setLastName("Test");
        user.setPassword(passwordEncoder.encode(TEST_PASSWORD));
        user.setAdmin(false);
        userRepository.save(user);

        // Test login with wrong password
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(TEST_EMAIL);
        loginRequest.setPassword("wrongpassword");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());

        // Test login with non-existent user
        loginRequest.setEmail("nonexistent@example.com");
        loginRequest.setPassword(TEST_PASSWORD);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }
}