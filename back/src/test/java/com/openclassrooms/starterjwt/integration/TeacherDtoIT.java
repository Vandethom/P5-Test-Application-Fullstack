package com.openclassrooms.starterjwt.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TeacherDtoIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtils jwtUtils;

    private User testUser;
    private Teacher testTeacher;
    private String jwtToken;
    private final String TEST_EMAIL = "teacherdto-test@example.com";
    private final String TEST_PASSWORD = "password123";

    @BeforeEach
    public void setup() {
        // Clean repositories
        teacherRepository.deleteAll();
        userRepository.deleteAll();
        
        // Create test user with admin privileges
        testUser = new User();
        testUser.setEmail(TEST_EMAIL);
        testUser.setFirstName("TeacherDto");
        testUser.setLastName("Integration");
        testUser.setPassword(passwordEncoder.encode(TEST_PASSWORD));
        testUser.setAdmin(true);
        userRepository.save(testUser);
        
        // Generate JWT token for authentication using UserDetailsImpl
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
            .id(testUser.getId())
            .username(testUser.getEmail())
            .firstName(testUser.getFirstName())
            .lastName(testUser.getLastName())
            .password(testUser.getPassword())
            .admin(testUser.isAdmin())
            .build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities());
        jwtToken = jwtUtils.generateJwtToken(authentication);
        
        // Create test teacher
        testTeacher = new Teacher();
        testTeacher.setFirstName("TestTeacher");
        testTeacher.setLastName("Integration");
        testTeacher.setCreatedAt(LocalDateTime.now());
        testTeacher.setUpdatedAt(LocalDateTime.now());
        teacherRepository.save(testTeacher);
    }

    @Test
    public void testGetAllTeachersWithDto() throws Exception {
        // Get all teachers and verify DTO mapping
        mockMvc.perform(get("/api/teacher")
                .header("Authorization", "Bearer " + jwtToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].firstName").value("TestTeacher"))
                .andExpect(jsonPath("$[0].lastName").value("Integration"));
    }

    @Test
    public void testGetTeacherWithDto() throws Exception {
        // Get teacher by ID and verify DTO mapping
        mockMvc.perform(get("/api/teacher/" + testTeacher.getId())
                .header("Authorization", "Bearer " + jwtToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testTeacher.getId()))
                .andExpect(jsonPath("$.firstName").value("TestTeacher"))
                .andExpect(jsonPath("$.lastName").value("Integration"))
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.updatedAt").exists());
    }

    @Test
    public void testCreateTeacherWithDto() throws Exception {
        // Create TeacherDto for creating a new teacher
        TeacherDto newTeacherDto = new TeacherDto();
        newTeacherDto.setFirstName("NewTeacher");
        newTeacherDto.setLastName("Created");
        LocalDateTime now = LocalDateTime.now();
        newTeacherDto.setCreatedAt(now);
        newTeacherDto.setUpdatedAt(now);

        // Create teacher using DTO
        MvcResult result = mockMvc.perform(post("/api/teacher")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newTeacherDto)))
                .andExpect(status().isOk())
                .andReturn();
        
        // Extract created teacher ID from response
        String content = result.getResponse().getContentAsString();
        TeacherDto createdTeacherDto = objectMapper.readValue(content, TeacherDto.class);
        
        // Verify teacher was created in database with DTO data
        Teacher createdTeacher = teacherRepository.findById(createdTeacherDto.getId())
                .orElseThrow(() -> new AssertionError("Teacher not found"));
        assertEquals("NewTeacher", createdTeacher.getFirstName());
        assertEquals("Created", createdTeacher.getLastName());
    }

    @Test
    public void testInvalidTeacherDto() throws Exception {
        // Create invalid TeacherDto (blank required fields)
        TeacherDto invalidDto = new TeacherDto();
        invalidDto.setFirstName(""); // Blank firstName which should fail validation
        invalidDto.setLastName("");   // Blank lastName which should fail validation

        // Attempt to create with invalid DTO
        mockMvc.perform(post("/api/teacher")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }
}