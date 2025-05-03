package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class SessionTest {
    
    @Test
    public void testGettersSetters() {
        Session session   = new Session();
        Date sessionDate  = new Date();
        LocalDateTime now = LocalDateTime.now();
        Teacher teacher   = new Teacher();
        teacher.setId(1L);
        List<User> users = new ArrayList<>();
        User user = new User();
        user.setId(1L);
        users.add(user);
        
        session.setId(1L);
        session.setName("Yoga Session");
        session.setDescription("Relaxing yoga session");
        session.setDate(sessionDate);
        session.setTeacher(teacher);
        session.setUsers(users);
        session.setCreatedAt(now);
        session.setUpdatedAt(now);
        
        assertEquals(1L, session.getId());
        assertEquals("Yoga Session", session.getName());
        assertEquals("Relaxing yoga session", session.getDescription());
        assertEquals(sessionDate, session.getDate());
        assertEquals(teacher, session.getTeacher());
        assertEquals(users, session.getUsers());
        assertEquals(now, session.getCreatedAt());
        assertEquals(now, session.getUpdatedAt());
    }
    
    @Test
    public void testEqualsAndHashCode() {
        Session session1 = new Session();
        session1.setId(1L);
        session1.setName("Yoga Session");
        session1.setDescription("Description 1");
        
        Session session2 = new Session();
        session2.setId(1L);
        session2.setName("Different Session");
        session2.setDescription("Description 2");
        
        // Equal because they have the same ID (as defined in @EqualsAndHashCode)
        assertEquals(session1, session2);
        assertEquals(session1.hashCode(), session2.hashCode());
        
        session2.setId(2L);
        assertNotEquals(session1, session2);
        assertNotEquals(session1.hashCode(), session2.hashCode());
    }
    
    @Test
    public void testBuilder() {
        Date sessionDate  = new Date();
        LocalDateTime now = LocalDateTime.now();
        Teacher teacher   = new Teacher();
        List<User> users  = new ArrayList<>();
        
        Session session = Session.builder()
                .id(1L)
                .name("Yoga Session")
                .description("Description")
                .date(sessionDate)
                .teacher(teacher)
                .users(users)
                .createdAt(now)
                .updatedAt(now)
                .build();
        
        assertEquals(1L, session.getId());
        assertEquals("Yoga Session", session.getName());
        assertEquals("Description", session.getDescription());
        assertEquals(sessionDate, session.getDate());
        assertEquals(teacher, session.getTeacher());
        assertEquals(users, session.getUsers());
        assertEquals(now, session.getCreatedAt());
        assertEquals(now, session.getUpdatedAt());
    }
    
    @Test
    public void testAllArgsConstructor() {
        Date sessionDate = new Date();
        LocalDateTime now = LocalDateTime.now();
        Teacher teacher = new Teacher();
        List<User> users = new ArrayList<>();
        
        Session session = new Session(
                1L, "Yoga Session", sessionDate, "Description", 
                teacher, users, now, now);
        
        assertEquals(1L, session.getId());
        assertEquals("Yoga Session", session.getName());
        assertEquals("Description", session.getDescription());
        assertEquals(sessionDate, session.getDate());
        assertEquals(teacher, session.getTeacher());
        assertEquals(users, session.getUsers());
        assertEquals(now, session.getCreatedAt());
        assertEquals(now, session.getUpdatedAt());
    }
    
    @Test
    public void testNoArgsConstructor() {
        Session session = new Session();
        
        assertNull(session.getId());
        assertNull(session.getName());
        assertNull(session.getDescription());
        assertNull(session.getDate());
        assertNull(session.getTeacher());
        assertNull(session.getUsers());
        assertNull(session.getCreatedAt());
        assertNull(session.getUpdatedAt());
    }
    
    @Test
    public void testToString() {
        Session session = new Session();
        session.setId(1L);
        session.setName("Yoga Session");
        session.setDescription("Description");
        
        String toStringResult = session.toString();
        
        assertTrue(toStringResult.contains("id=1"));
        assertTrue(toStringResult.contains("name=Yoga Session"));
        assertTrue(toStringResult.contains("description=Description"));
    }
    
    @Test
    public void testChaining() {
        Date sessionDate = new Date();
        Teacher teacher = new Teacher();
        List<User> users = Arrays.asList(new User());
        
        Session session = new Session()
                .setId(1L)
                .setName("Yoga Session")
                .setDescription("Description")
                .setDate(sessionDate)
                .setTeacher(teacher)
                .setUsers(users);
        
        assertEquals(1L, session.getId());
        assertEquals("Yoga Session", session.getName());
        assertEquals("Description", session.getDescription());
        assertEquals(sessionDate, session.getDate());
        assertEquals(teacher, session.getTeacher());
        assertEquals(users, session.getUsers());
    }
    
    @Test
    public void testUsersListManipulation() {
        Session session = new Session();
        List<User> users = new ArrayList<>();
        
        User user1 = new User();
        user1.setId(1L);
        
        User user2 = new User();
        user2.setId(2L);
        
        users.add(user1);
        session.setUsers(users);
        
        assertEquals(1, session.getUsers().size());
        assertEquals(user1, session.getUsers().get(0));
        
        // Add another user to the list
        session.getUsers().add(user2);
        assertEquals(2, session.getUsers().size());
        assertTrue(session.getUsers().contains(user1));
        assertTrue(session.getUsers().contains(user2));
    }
    
    @Test
    public void testBuilderWithNulls() {
        Session session = Session.builder()
                .id(null)
                .name(null)
                .description(null)
                .date(null)
                .teacher(null)
                .users(null)
                .createdAt(null)
                .updatedAt(null)
                .build();
        
        assertNull(session.getId());
        assertNull(session.getName());
        assertNull(session.getDescription());
        assertNull(session.getDate());
        assertNull(session.getTeacher());
        assertNull(session.getUsers());
        assertNull(session.getCreatedAt());
        assertNull(session.getUpdatedAt());
    }
    
    @Test
    public void testSetNullUsers() {
        Session session = new Session();
        session.setUsers(null);
        assertNull(session.getUsers());
    }
    
    @Test
    public void testSetNullTeacher() {
        Session session = new Session();
        session.setTeacher(null);
        assertNull(session.getTeacher());
    }
    
    @Test
    public void testEqualsWithNulls() {
        Session session1 = new Session();
        session1.setId(1L);
        
        Session session2 = new Session();
        session2.setId(null);
        
        assertNotEquals(session1, session2);
        assertNotEquals(session2, session1);
    }
    
    @Test
    public void testEqualsSameObject() {
        Session session = new Session();
        assertEquals(session, session);
    }
    
    @Test
    public void testEqualsWithDifferentClass() {
        Session session = new Session();
        assertNotEquals(session, new Object());
    }
    
    @Test
    public void testEqualsWithNull() {
        Session session = new Session();
        assertNotEquals(session, null);
    }
}