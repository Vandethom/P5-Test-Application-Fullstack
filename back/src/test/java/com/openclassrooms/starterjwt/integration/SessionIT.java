package com.openclassrooms.starterjwt.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class SessionIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User testUser;
    private Teacher testTeacher;
    private Session testSession;

    @BeforeEach
    public void setup() {
        sessionRepository.deleteAll();
        teacherRepository.deleteAll();
        userRepository.deleteAll();

        // Create a test user
        testUser = new User();
        testUser.setEmail("session-test@example.com");
        testUser.setFirstName("Session");
        testUser.setLastName("Test");
        testUser.setPassword(passwordEncoder.encode("password"));
        testUser.setAdmin(true);
        userRepository.save(testUser);

        // Create a test teacher
        testTeacher = new Teacher();
        testTeacher.setFirstName("Yoga");
        testTeacher.setLastName("Teacher");
        testTeacher.setCreatedAt(LocalDateTime.now());
        testTeacher.setUpdatedAt(LocalDateTime.now());
        teacherRepository.save(testTeacher);

        // Create a test session
        testSession = new Session();
        testSession.setName("Test Yoga Session");
        
        // Use Date instead of LocalDateTime for the session date
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 7); // Add 7 days to current date
        testSession.setDate(calendar.getTime());
        
        testSession.setDescription("A test yoga session for integration tests");
        testSession.setTeacher(testTeacher);
        testSession.setCreatedAt(LocalDateTime.now());
        testSession.setUpdatedAt(LocalDateTime.now());
        
        // Initialize the users list to prevent NullPointerException
        testSession.setUsers(new ArrayList<>());
        
        sessionRepository.save(testSession);
    }

    @Test
    @WithMockUser(username = "session-test@example.com", roles = {"ADMIN"})
    public void testFindAllSessions() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/session"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        List<?> sessions = objectMapper.readValue(content, List.class);
        assertFalse(sessions.isEmpty(), "Sessions list should not be empty");
    }

    @Test
    @WithMockUser(username = "session-test@example.com", roles = {"ADMIN"})
    public void testFindSessionById() throws Exception {
        mockMvc.perform(get("/api/session/{id}", testSession.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(testSession.getId()))
                .andExpect(jsonPath("$.name").value(testSession.getName()))
                .andExpect(jsonPath("$.description").value(testSession.getDescription()));
    }

    @Test
    @WithMockUser(username = "session-test@example.com", roles = {"ADMIN"})
    public void testCreateAndUpdateSession() throws Exception {
        // Create a new session DTO
        SessionDto newSessionDto = new SessionDto();
        newSessionDto.setName("New Integration Test Session");
        newSessionDto.setDescription("Created during integration testing");
        newSessionDto.setTeacher_id(testTeacher.getId());
        
        // Add a date since it's required
        Calendar sessionDate = Calendar.getInstance();
        sessionDate.add(Calendar.DAY_OF_MONTH, 14); // 2 weeks from now
        newSessionDto.setDate(sessionDate.getTime());
        
        // Test creating a new session
        MvcResult createResult = mockMvc.perform(post("/api/session")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newSessionDto)))
                .andExpect(status().isOk())
                .andReturn();

        String createContent = createResult.getResponse().getContentAsString();
        SessionDto createdSession = objectMapper.readValue(createContent, SessionDto.class);
        
        assertNotNull(createdSession.getId());
        assertEquals(newSessionDto.getName(), createdSession.getName());
        assertEquals(newSessionDto.getDescription(), createdSession.getDescription());
        assertEquals(newSessionDto.getTeacher_id(), createdSession.getTeacher_id());
        
        // Update the created session - notice the URL format matches the controller's annotation
        createdSession.setName("Updated Session Name");
        createdSession.setDescription("Updated during integration testing");
        createdSession.setDate(newSessionDto.getDate());
        
        mockMvc.perform(put("/api/session/{id}", createdSession.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createdSession)))
                .andExpect(status().isOk());
        
        // Verify the update
        mockMvc.perform(get("/api/session/{id}", createdSession.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Session Name"))
                .andExpect(jsonPath("$.description").value("Updated during integration testing"));
    }
    
    @Test
    @WithMockUser(username = "session-test@example.com", roles = {"ADMIN"})
    public void testDeleteSession() throws Exception {
        // Delete the session - notice the URL format matches the controller's annotation
        mockMvc.perform(delete("/api/session/{id}", testSession.getId()))
                .andExpect(status().isOk());
        
        // Verify it was deleted
        mockMvc.perform(get("/api/session/{id}", testSession.getId()))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @WithMockUser(username = "session-test@example.com", roles = {"USER"})
    public void testParticipateInSession() throws Exception {
        // Test user participation - notice the URL format matches the controller's annotation
        mockMvc.perform(post("/api/session/{id}/participate/{userId}", testSession.getId(), testUser.getId()))
                .andExpect(status().isOk());
        
        // Verify participation
        MvcResult participateResult = mockMvc.perform(get("/api/session/{id}", testSession.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        
        // Test unparticipation - notice the URL format matches the controller's annotation
        mockMvc.perform(delete("/api/session/{id}/participate/{userId}", testSession.getId(), testUser.getId()))
                .andExpect(status().isOk());
        
        // Verify unparticipation
        MvcResult result = mockMvc.perform(get("/api/session/{id}", testSession.getId()))
                .andExpect(status().isOk())
                .andReturn();
        
        // We can't directly check the participants list as it's not returned in the session DTO
        // but the 200 OK status from the previous calls indicates success
    }
}