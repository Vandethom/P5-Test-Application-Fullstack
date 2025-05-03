package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TeacherMapperTest {
    
    private TeacherMapper mapper;
    private Teacher teacher;
    private TeacherDto teacherDto;
    private LocalDateTime now;

    @BeforeEach
    public void setUp() {
        mapper = Mappers.getMapper(TeacherMapper.class);
        now = LocalDateTime.now();
        
        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        teacher.setCreatedAt(now);
        teacher.setUpdatedAt(now);
        
        teacherDto = new TeacherDto();
        teacherDto.setId(1L);
        teacherDto.setFirstName("John");
        teacherDto.setLastName("Doe");
        teacherDto.setCreatedAt(now);
        teacherDto.setUpdatedAt(now);
    }

    @Test
    public void testToEntity() {
        Teacher entity = mapper.toEntity(teacherDto);
        assertNotNull(entity);
        assertEquals(teacherDto.getId(), entity.getId());
        assertEquals(teacherDto.getFirstName(), entity.getFirstName());
        assertEquals(teacherDto.getLastName(), entity.getLastName());
        assertEquals(teacherDto.getCreatedAt(), entity.getCreatedAt());
        assertEquals(teacherDto.getUpdatedAt(), entity.getUpdatedAt());
    }

    @Test
    public void testToDto() {
        TeacherDto dto = mapper.toDto(teacher);
        assertNotNull(dto);
        assertEquals(teacher.getId(), dto.getId());
        assertEquals(teacher.getFirstName(), dto.getFirstName());
        assertEquals(teacher.getLastName(), dto.getLastName());
        assertEquals(teacher.getCreatedAt(), dto.getCreatedAt());
        assertEquals(teacher.getUpdatedAt(), dto.getUpdatedAt());
    }

    @Test
    public void testToEntityList() {
        List<TeacherDto> dtoList = Arrays.asList(teacherDto);
        List<Teacher> entityList = mapper.toEntity(dtoList);
        assertNotNull(entityList);
        assertEquals(1, entityList.size());
        assertEquals(teacherDto.getId(), entityList.get(0).getId());
        assertEquals(teacherDto.getFirstName(), entityList.get(0).getFirstName());
    }

    @Test
    public void testToDtoList() {
        List<Teacher> entityList = Arrays.asList(teacher);
        List<TeacherDto> dtoList = mapper.toDto(entityList);
        assertNotNull(dtoList);
        assertEquals(1, dtoList.size());
        assertEquals(teacher.getId(), dtoList.get(0).getId());
        assertEquals(teacher.getFirstName(), dtoList.get(0).getFirstName());
    }

    @Test
    public void testToEntityWithNull() {
        Teacher entity = mapper.toEntity((TeacherDto) null);
        assertNull(entity);
    }

    @Test
    public void testToDtoWithNull() {
        TeacherDto dto = mapper.toDto((Teacher) null);
        assertNull(dto);
    }

    @Test
    public void testToEntityListWithNull() {
        List<Teacher> entityList = mapper.toEntity((List<TeacherDto>) null);
        assertNull(entityList);
    }

    @Test
    public void testToDtoListWithNull() {
        List<TeacherDto> dtoList = mapper.toDto((List<Teacher>) null);
        assertNull(dtoList);
    }

    @Test
    public void testToEntityEmptyList() {
        List<TeacherDto> emptyDtoList = new ArrayList<>();
        List<Teacher> entityList = mapper.toEntity(emptyDtoList);
        assertNotNull(entityList);
        assertTrue(entityList.isEmpty());
    }

    @Test
    public void testToDtoEmptyList() {
        List<Teacher> emptyEntityList = new ArrayList<>();
        List<TeacherDto> dtoList = mapper.toDto(emptyEntityList);
        assertNotNull(dtoList);
        assertTrue(dtoList.isEmpty());
    }
}