package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

public class SessionMapperTest {
    private SessionMapper mapper;
    private TeacherService teacherService;
    private UserService userService;
    private Session session;
    private SessionDto sessionDto;
    private Teacher teacher;
    private User user1, user2;

    @BeforeEach
    public void setUp() {
        teacherService = mock(TeacherService.class);
        userService = mock(UserService.class);
        mapper = org.mapstruct.factory.Mappers.getMapper(SessionMapper.class);
        ReflectionTestUtils.setField(mapper, "teacherService", teacherService);
        ReflectionTestUtils.setField(mapper, "userService", userService);
        
        // Create a teacher
        teacher = new Teacher();
        teacher.setId(2L);
        teacher.setFirstName("Jane");
        teacher.setLastName("Doe");
        
        // Create users
        user1 = new User();
        user1.setId(1L);
        user1.setEmail("user1@example.com");
        user1.setFirstName("User");
        user1.setLastName("One");
        user1.setPassword("password");
        
        user2 = new User();
        user2.setId(2L);
        user2.setEmail("user2@example.com");
        user2.setFirstName("User");
        user2.setLastName("Two");
        user2.setPassword("password");
        
        // Create a session
        session = new Session();
        session.setId(1L);
        session.setName("Session Name");
        session.setDescription("Session Description");
        session.setDate(new Date());
        session.setTeacher(teacher);
        session.setUsers(Arrays.asList(user1, user2));
        
        // Create a session DTO
        sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setName("Session Name");
        sessionDto.setDescription("Session Description");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(2L);
        sessionDto.setUsers(Arrays.asList(1L, 2L));
        
        // Configure mocks
        when(teacherService.findById(eq(2L))).thenReturn(teacher);
        when(userService.findById(eq(1L))).thenReturn(user1);
        when(userService.findById(eq(2L))).thenReturn(user2);
    }

    @Test
    public void testToEntity_Complete() {
        Session entity = mapper.toEntity(sessionDto);
        
        // Verify basic mapping
        assertNotNull(entity);
        assertEquals(sessionDto.getId(), entity.getId());
        assertEquals(sessionDto.getName(), entity.getName());
        assertEquals(sessionDto.getDescription(), entity.getDescription());
        assertEquals(sessionDto.getDate(), entity.getDate());
        
        // Verify teacher mapping
        assertNotNull(entity.getTeacher());
        assertEquals(teacher.getId(), entity.getTeacher().getId());
        verify(teacherService, times(1)).findById(sessionDto.getTeacher_id());
        
        // Verify users mapping
        assertNotNull(entity.getUsers());
        assertEquals(2, entity.getUsers().size());
        assertEquals(user1.getId(), entity.getUsers().get(0).getId());
        assertEquals(user2.getId(), entity.getUsers().get(1).getId());
        verify(userService, times(1)).findById(1L);
        verify(userService, times(1)).findById(2L);
    }
    
    @Test
    public void testToDto_Complete() {
        SessionDto dto = mapper.toDto(session);
        
        // Verify basic mapping
        assertNotNull(dto);
        assertEquals(session.getId(), dto.getId());
        assertEquals(session.getName(), dto.getName());
        assertEquals(session.getDescription(), dto.getDescription());
        assertEquals(session.getDate(), dto.getDate());
        
        // Verify teacher_id mapping
        assertEquals(teacher.getId(), dto.getTeacher_id());
        
        // Verify users mapping
        assertNotNull(dto.getUsers());
        assertEquals(2, dto.getUsers().size());
        assertTrue(dto.getUsers().contains(1L));
        assertTrue(dto.getUsers().contains(2L));
    }

    @Test
    public void testToEntity_NullTeacherId() {
        sessionDto.setTeacher_id(null);
        
        Session entity = mapper.toEntity(sessionDto);
        
        assertNotNull(entity);
        assertNull(entity.getTeacher());
        verify(teacherService, times(0)).findById(anyLong());
    }

    @Test
    public void testToEntity_NullUsersList() {
        sessionDto.setUsers(null);
        
        Session entity = mapper.toEntity(sessionDto);
        
        assertNotNull(entity);
        assertNotNull(entity.getUsers());
        assertTrue(entity.getUsers().isEmpty());
    }
    
    @Test
    public void testToDto_NullTeacher() {
        session.setTeacher(null);
        
        SessionDto dto = mapper.toDto(session);
        
        assertNotNull(dto);
        assertNull(dto.getTeacher_id());
    }
    
    @Test
    public void testToDto_NullUsersList() {
        session.setUsers(null);
        
        SessionDto dto = mapper.toDto(session);
        
        assertNotNull(dto);
        assertNotNull(dto.getUsers());
        assertTrue(dto.getUsers().isEmpty());
    }
    
    @Test
    public void testToEntity_EmptyUsersList() {
        sessionDto.setUsers(Collections.emptyList());
        
        Session entity = mapper.toEntity(sessionDto);
        
        assertNotNull(entity);
        assertNotNull(entity.getUsers());
        assertTrue(entity.getUsers().isEmpty());
    }
    
    @Test
    public void testToDto_EmptyUsersList() {
        session.setUsers(Collections.emptyList());
        
        SessionDto dto = mapper.toDto(session);
        
        assertNotNull(dto);
        assertNotNull(dto.getUsers());
        assertTrue(dto.getUsers().isEmpty());
    }
    
    @Test
    public void testToEntity_NullDto() {
        Session entity = mapper.toEntity((SessionDto) null);
        assertNull(entity);
    }
    
    @Test
    public void testToDto_NullEntity() {
        SessionDto dto = mapper.toDto((Session) null);
        assertNull(dto);
    }
    
    @Test
    public void testToEntityList() {
        List<SessionDto> dtoList = Arrays.asList(sessionDto);
        
        List<Session> entityList = mapper.toEntity(dtoList);
        
        assertNotNull(entityList);
        assertEquals(1, entityList.size());
        assertEquals(sessionDto.getId(), entityList.get(0).getId());
        assertEquals(sessionDto.getName(), entityList.get(0).getName());
    }
    
    @Test
    public void testToDtoList() {
        List<Session> entityList = Arrays.asList(session);
        
        List<SessionDto> dtoList = mapper.toDto(entityList);
        
        assertNotNull(dtoList);
        assertEquals(1, dtoList.size());
        assertEquals(session.getId(), dtoList.get(0).getId());
        assertEquals(session.getName(), dtoList.get(0).getName());
    }
    
    @Test
    public void testToEntityList_Null() {
        List<Session> entityList = mapper.toEntity((List<SessionDto>) null);
        assertNull(entityList);
    }
    
    @Test
    public void testToDtoList_Null() {
        List<SessionDto> dtoList = mapper.toDto((List<Session>) null);
        assertNull(dtoList);
    }
    
    @Test
    public void testToEntityList_Empty() {
        List<SessionDto> emptyDtoList = new ArrayList<>();
        
        List<Session> entityList = mapper.toEntity(emptyDtoList);
        
        assertNotNull(entityList);
        assertTrue(entityList.isEmpty());
    }
    
    @Test
    public void testToDtoList_Empty() {
        List<Session> emptyEntityList = new ArrayList<>();
        
        List<SessionDto> dtoList = mapper.toDto(emptyEntityList);
        
        assertNotNull(dtoList);
        assertTrue(dtoList.isEmpty());
    }
    
    @Test
    public void testToEntity_UserNotFound() {
        // Setup a scenario where one user is not found
        sessionDto.setUsers(Arrays.asList(1L, 3L)); // User with ID 3 doesn't exist
        when(userService.findById(3L)).thenReturn(null);
        
        Session entity = mapper.toEntity(sessionDto);
        
        assertNotNull(entity);
        assertNotNull(entity.getUsers());
        assertEquals(2, entity.getUsers().size()); 
        assertEquals(user1.getId(), entity.getUsers().get(0).getId());
        assertNull(entity.getUsers().get(1)); // The second element should be null
    }
    
    @Test
    public void testToEntity_MixedTeacherAndUserCase() {
        // Test with null teacher and some users
        sessionDto.setTeacher_id(null);
        sessionDto.setUsers(Arrays.asList(1L)); // Only one valid user
        
        Session entity = mapper.toEntity(sessionDto);
        
        assertNotNull(entity);
        assertNull(entity.getTeacher());
        assertNotNull(entity.getUsers());
        assertEquals(1, entity.getUsers().size());
        assertEquals(user1.getId(), entity.getUsers().get(0).getId());
    }
    
    @Test
    public void testToDto_MixedTeacherAndUserCase() {
        // Test with null teacher and some users
        session.setTeacher(null);
        session.setUsers(Arrays.asList(user1)); // Only one user
        
        SessionDto dto = mapper.toDto(session);
        
        assertNotNull(dto);
        assertNull(dto.getTeacher_id());
        assertNotNull(dto.getUsers());
        assertEquals(1, dto.getUsers().size());
        assertEquals(user1.getId(), dto.getUsers().get(0));
    }
}
