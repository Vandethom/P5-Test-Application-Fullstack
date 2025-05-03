package com.openclassrooms.starterjwt.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TeacherIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TeacherRepository teacherRepository;

    private Teacher testTeacher;

    @BeforeEach
    public void setup() {
        teacherRepository.deleteAll();

        // Create a test teacher directly through repository
        testTeacher = new Teacher();
        testTeacher.setFirstName("Integration");
        testTeacher.setLastName("Teacher");
        testTeacher.setCreatedAt(LocalDateTime.now());
        testTeacher.setUpdatedAt(LocalDateTime.now());
        teacherRepository.save(testTeacher);
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testFindAllTeachers() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/teacher"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        List<?> teachers = objectMapper.readValue(content, List.class);
        assertFalse(teachers.isEmpty(), "Teachers list should not be empty");
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testFindTeacherById() throws Exception {
        mockMvc.perform(get("/api/teacher/{id}", testTeacher.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(testTeacher.getId()))
                .andExpect(jsonPath("$.firstName").value(testTeacher.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(testTeacher.getLastName()));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testFindTeacherByInvalidId() throws Exception {
        // Test with invalid ID format
        mockMvc.perform(get("/api/teacher/invalid-id"))
                .andExpect(status().isBadRequest());
        
        // Test with non-existent ID
        mockMvc.perform(get("/api/teacher/9999"))
                .andExpect(status().isNotFound());
    }
}