package com.openclassrooms.starterjwt.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserDtoIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtils jwtUtils;

    private User testUser;
    private User regularUser;
    private String adminJwtToken;
    private String userJwtToken;
    private final String ADMIN_EMAIL = "userdto-admin@example.com";
    private final String USER_EMAIL = "userdto-user@example.com";
    private final String TEST_PASSWORD = "password123";

    @BeforeEach
    public void setup() {
        // Clean repository
        userRepository.deleteAll();
        
        // Create admin user
        testUser = new User();
        testUser.setEmail(ADMIN_EMAIL);
        testUser.setFirstName("UserDto");
        testUser.setLastName("Admin");
        testUser.setPassword(passwordEncoder.encode(TEST_PASSWORD));
        testUser.setAdmin(true);
        testUser.setCreatedAt(LocalDateTime.now());
        testUser.setUpdatedAt(LocalDateTime.now());
        userRepository.save(testUser);
        
        // Create regular user
        regularUser = new User();
        regularUser.setEmail(USER_EMAIL);
        regularUser.setFirstName("Regular");
        regularUser.setLastName("User");
        regularUser.setPassword(passwordEncoder.encode(TEST_PASSWORD));
        regularUser.setAdmin(false);
        regularUser.setCreatedAt(LocalDateTime.now());
        regularUser.setUpdatedAt(LocalDateTime.now());
        userRepository.save(regularUser);
        
        // Generate JWT tokens for authentication
        // For admin user
        UserDetailsImpl adminUserDetails = UserDetailsImpl.builder()
            .id(testUser.getId())
            .username(testUser.getEmail())
            .firstName(testUser.getFirstName())
            .lastName(testUser.getLastName())
            .password(testUser.getPassword())
            .admin(testUser.isAdmin())
            .build();
        Authentication adminAuthentication = new UsernamePasswordAuthenticationToken(
                adminUserDetails, null, adminUserDetails.getAuthorities());
        adminJwtToken = jwtUtils.generateJwtToken(adminAuthentication);
        
        // For regular user
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
            .id(regularUser.getId())
            .username(regularUser.getEmail())
            .firstName(regularUser.getFirstName())
            .lastName(regularUser.getLastName())
            .password(regularUser.getPassword())
            .admin(regularUser.isAdmin())
            .build();
        Authentication userAuthentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        userJwtToken = jwtUtils.generateJwtToken(userAuthentication);
    }

    @Test
    public void testGetUserWithDto() throws Exception {
        // Get user by ID and verify DTO mapping
        MvcResult result = mockMvc.perform(get("/api/user/" + regularUser.getId())
                .header("Authorization", "Bearer " + adminJwtToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(regularUser.getId()))
                .andExpect(jsonPath("$.email").value(USER_EMAIL))
                .andExpect(jsonPath("$.firstName").value("Regular"))
                .andExpect(jsonPath("$.lastName").value("User"))
                .andExpect(jsonPath("$.admin").value(false))
                .andReturn();

        // Parse the response to ensure DTO is correctly mapped
        String content = result.getResponse().getContentAsString();
        UserDto returnedDto = objectMapper.readValue(content, UserDto.class);
        
        assertEquals(regularUser.getId(), returnedDto.getId());
        assertEquals(regularUser.getEmail(), returnedDto.getEmail());
        assertEquals(regularUser.getFirstName(), returnedDto.getFirstName());
        assertEquals(regularUser.getLastName(), returnedDto.getLastName());
        assertEquals(regularUser.isAdmin(), returnedDto.isAdmin());
    }

    @Test
    public void testGetMeWithDto() throws Exception {
        // Test /me endpoint which returns current user as DTO
        mockMvc.perform(get("/api/auth/me")
                .header("Authorization", "Bearer " + userJwtToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(regularUser.getId()))
                .andExpect(jsonPath("$.email").value(USER_EMAIL))
                .andExpect(jsonPath("$.firstName").value("Regular"))
                .andExpect(jsonPath("$.lastName").value("User"));
    }
    
    @Test
    public void testRegisterWithDto() throws Exception {
        // Create UserDto for registration
        UserDto newUserDto = new UserDto();
        newUserDto.setEmail("new-user@example.com");
        newUserDto.setPassword(TEST_PASSWORD);
        newUserDto.setFirstName("New");
        newUserDto.setLastName("User");
        newUserDto.setAdmin(false);
        
        // Register user using DTO
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUserDto)))
                .andExpect(status().isOk());
        
        // Verify user was created in database from DTO data
        Optional<User> createdUser = userRepository.findByEmail("new-user@example.com");
        assertTrue(createdUser.isPresent());
        assertEquals("New", createdUser.get().getFirstName());
        assertEquals("User", createdUser.get().getLastName());
        assertFalse(createdUser.get().isAdmin());
    }

    @Test
    public void testUpdateUserWithDto() throws Exception {
        // Create UserDto with updated information
        UserDto updateDto = new UserDto();
        updateDto.setId(regularUser.getId());
        updateDto.setEmail(regularUser.getEmail());  
        updateDto.setFirstName("Updated");
        updateDto.setLastName("UserName");
        updateDto.setAdmin(false);
        
        // Update user using DTO
        mockMvc.perform(put("/api/user/" + regularUser.getId())
                .header("Authorization", "Bearer " + userJwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk());
        
        // Verify user was updated in database with DTO data
        User updatedUser = userRepository.findById(regularUser.getId())
                .orElseThrow(() -> new AssertionError("User not found"));
        assertEquals("Updated", updatedUser.getFirstName());
        assertEquals("UserName", updatedUser.getLastName());
    }

    @Test
    public void testInvalidUserDto() throws Exception {
        // Create invalid UserDto (missing required fields)
        UserDto invalidDto = new UserDto();
        invalidDto.setEmail("");          // Empty email which should fail validation
        invalidDto.setFirstName("");  // Empty firstName which should fail validation
        invalidDto.setLastName("");    // Empty lastName which should fail validation
        
        // Attempt to register with invalid DTO
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetUserById_Success() throws Exception {
        // Test getting the admin user by ID with admin privileges
        mockMvc.perform(get("/api/user/" + testUser.getId())
                .header("Authorization", "Bearer " + adminJwtToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testUser.getId()))
                .andExpect(jsonPath("$.email").value(ADMIN_EMAIL))
                .andExpect(jsonPath("$.firstName").value("UserDto"))
                .andExpect(jsonPath("$.lastName").value("Admin"))
                .andExpect(jsonPath("$.admin").value(true));
    }

    @Test
    public void testGetUserById_AsUser_Success() throws Exception {
        // Test getting own user by ID with regular user privileges
        mockMvc.perform(get("/api/user/" + regularUser.getId())
                .header("Authorization", "Bearer " + userJwtToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(regularUser.getId()))
                .andExpect(jsonPath("$.email").value(USER_EMAIL))
                .andExpect(jsonPath("$.firstName").value("Regular"))
                .andExpect(jsonPath("$.lastName").value("User"))
                .andExpect(jsonPath("$.admin").value(false));
    }

    @Test
    public void testGetUserById_AsUserAccessingOtherUser_Forbidden() throws Exception {
        // Test access denial when a regular user tries to access another user's info
        mockMvc.perform(get("/api/user/" + testUser.getId())
                .header("Authorization", "Bearer " + userJwtToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testGetUserById_NotFound() throws Exception {
        // Test scenario with non-existent user ID
        Long nonExistentId = 999L;
        mockMvc.perform(get("/api/user/" + nonExistentId)
                .header("Authorization", "Bearer " + adminJwtToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteUser_AsAdmin_Success() throws Exception {
        // Test deleting a user with admin privileges
        mockMvc.perform(delete("/api/user/" + regularUser.getId())
                .header("Authorization", "Bearer " + adminJwtToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Verify user was deleted
        Optional<User> deletedUser = userRepository.findById(regularUser.getId());
        assertFalse(deletedUser.isPresent());
    }

    @Test
    public void testDeleteUser_AsUser_Success() throws Exception {
        // Test deleting own account with regular user privileges
        mockMvc.perform(delete("/api/user/" + regularUser.getId())
                .header("Authorization", "Bearer " + userJwtToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Verify user was deleted
        Optional<User> deletedUser = userRepository.findById(regularUser.getId());
        assertFalse(deletedUser.isPresent());
    }

    @Test
    public void testDeleteUser_AsUserDeletingOtherUser_Forbidden() throws Exception {
        // Test access denial when a regular user tries to delete another user
        mockMvc.perform(delete("/api/user/" + testUser.getId())
                .header("Authorization", "Bearer " + userJwtToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        // Verify user was not deleted
        Optional<User> notDeletedUser = userRepository.findById(testUser.getId());
        assertTrue(notDeletedUser.isPresent());
    }

    @Test
    public void testDeleteUser_NotFound() throws Exception {
        // Test deleting non-existent user
        Long nonExistentId = 999L;
        mockMvc.perform(delete("/api/user/" + nonExistentId)
                .header("Authorization", "Bearer " + adminJwtToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetUserMe_Success() throws Exception {
        // Test getting current user info with admin token
        mockMvc.perform(get("/api/user/me")
                .header("Authorization", "Bearer " + adminJwtToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testUser.getId()))
                .andExpect(jsonPath("$.email").value(ADMIN_EMAIL))
                .andExpect(jsonPath("$.admin").value(true));
    }
}