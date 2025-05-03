package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TeacherControllerTest {
    @Mock
    TeacherService teacherService;
    @Mock
    TeacherMapper teacherMapper;
    @InjectMocks
    TeacherController teacherController;

    private Teacher teacher;
    private TeacherDto teacherDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        teacher.setCreatedAt(LocalDateTime.now());
        teacher.setUpdatedAt(LocalDateTime.now());
        teacherDto = new TeacherDto();
        teacherDto.setId(1L);
        teacherDto.setFirstName("John");
        teacherDto.setLastName("Doe");
        teacherDto.setCreatedAt(teacher.getCreatedAt());
        teacherDto.setUpdatedAt(teacher.getUpdatedAt());
    }

    @Test
    public void testFindById() {
        when(teacherService.findById(1L)).thenReturn(teacher);
        when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);
        ResponseEntity<?> response = teacherController.findById("1");
        assertEquals(200, response.getStatusCodeValue());
        TeacherDto body = (TeacherDto) response.getBody();
        assertNotNull(body);
        assertEquals(1L, body.getId());
        assertEquals("John", body.getFirstName());
        assertEquals("Doe", body.getLastName());
    }

    @Test
    public void testFindAll() {
        when(teacherService.findAll()).thenReturn(Arrays.asList(teacher));
        when(teacherMapper.toDto(anyList())).thenReturn(Arrays.asList(teacherDto));
        ResponseEntity<?> response = teacherController.findAll();
        assertEquals(200, response.getStatusCodeValue());
        List<TeacherDto> body = (List<TeacherDto>) response.getBody();
        assertNotNull(body);
        assertFalse(body.isEmpty());
        assertEquals(teacherDto.getId(), body.get(0).getId());
    }

    @Test
    public void testFindByIdNotFound() {
        when(teacherService.findById(2L)).thenReturn(null);
        ResponseEntity<?> response = teacherController.findById("2");
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void testFindByIdBadRequest() {
        ResponseEntity<?> response = teacherController.findById("notANumber");
        assertEquals(400, response.getStatusCodeValue());
    }
}
