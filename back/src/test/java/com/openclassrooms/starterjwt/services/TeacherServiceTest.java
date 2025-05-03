package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {
    @Mock
    private TeacherRepository teacherRepository;
    @InjectMocks
    private TeacherService teacherService;
    private Teacher testTeacher;

    @BeforeEach
    public void setUp() {
        testTeacher = new Teacher();
        testTeacher.setId(1L);
        testTeacher.setFirstName("John");
        testTeacher.setLastName("Doe");
    }

    @Test
    public void testFindByIdSuccess() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(testTeacher));
        Teacher found = teacherService.findById(1L);
        assertNotNull(found);
        assertEquals(1L, found.getId());
    }

    @Test
    public void testFindByIdNotFound() {
        when(teacherRepository.findById(2L)).thenReturn(Optional.empty());
        Teacher found = teacherService.findById(2L);
        assertNull(found);
    }

    @Test
    public void testFindAll() {
        List<Teacher> teachers = Arrays.asList(testTeacher);
        when(teacherRepository.findAll()).thenReturn(teachers);
        List<Teacher> found = teacherService.findAll();
        assertNotNull(found);
        assertEquals(1, found.size());
        assertEquals("John", found.get(0).getFirstName());
    }
}
