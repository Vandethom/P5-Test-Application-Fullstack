package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class TeacherTest {
    
    @Test
    public void testGettersSetters() {
        Teacher teacher = new Teacher();
        LocalDateTime now = LocalDateTime.now();
        
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Smith");
        teacher.setCreatedAt(now);
        teacher.setUpdatedAt(now);
        
        assertEquals(1L, teacher.getId());
        assertEquals("John", teacher.getFirstName());
        assertEquals("Smith", teacher.getLastName());
        assertEquals(now, teacher.getCreatedAt());
        assertEquals(now, teacher.getUpdatedAt());
    }
    
    @Test
    public void testEqualsAndHashCode() {
        Teacher teacher1 = new Teacher();
        teacher1.setId(1L);
        teacher1.setFirstName("John");
        teacher1.setLastName("Smith");
        
        Teacher teacher2 = new Teacher();
        teacher2.setId(1L);
        teacher2.setFirstName("Different");
        teacher2.setLastName("Name");
        
        // Equal because they have the same ID (as defined in @EqualsAndHashCode)
        assertEquals(teacher1, teacher2);
        assertEquals(teacher1.hashCode(), teacher2.hashCode());
        
        teacher2.setId(2L);
        assertNotEquals(teacher1, teacher2);
        assertNotEquals(teacher1.hashCode(), teacher2.hashCode());
    }
    
    @Test
    public void testBuilder() {
        LocalDateTime now = LocalDateTime.now();
        Teacher teacher = Teacher.builder()
                .id(1L)
                .firstName("John")
                .lastName("Smith")
                .createdAt(now)
                .updatedAt(now)
                .build();
        
        assertEquals(1L, teacher.getId());
        assertEquals("John", teacher.getFirstName());
        assertEquals("Smith", teacher.getLastName());
        assertEquals(now, teacher.getCreatedAt());
        assertEquals(now, teacher.getUpdatedAt());
    }
    
    @Test
    public void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        Teacher teacher = new Teacher(1L, "Smith", "John", now, now);
        
        assertEquals(1L, teacher.getId());
        assertEquals("John", teacher.getFirstName());
        assertEquals("Smith", teacher.getLastName());
        assertEquals(now, teacher.getCreatedAt());
        assertEquals(now, teacher.getUpdatedAt());
    }
    
    @Test
    public void testNoArgsConstructor() {
        Teacher teacher = new Teacher();
        
        assertNull(teacher.getId());
        assertNull(teacher.getFirstName());
        assertNull(teacher.getLastName());
        assertNull(teacher.getCreatedAt());
        assertNull(teacher.getUpdatedAt());
    }
    
    @Test
    public void testToString() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Smith");
        
        String toStringResult = teacher.toString();
        
        assertTrue(toStringResult.contains("id=1"));
        assertTrue(toStringResult.contains("firstName=John"));
        assertTrue(toStringResult.contains("lastName=Smith"));
    }
    
    @Test
    public void testChaining() {
        Teacher teacher = new Teacher()
                .setId(1L)
                .setFirstName("John")
                .setLastName("Smith");
        
        assertEquals(1L, teacher.getId());
        assertEquals("John", teacher.getFirstName());
        assertEquals("Smith", teacher.getLastName());
    }
    
    @Test
    public void testBuilderWithNulls() {
        Teacher teacher = Teacher.builder()
                .id(null)
                .firstName(null)
                .lastName(null)
                .createdAt(null)
                .updatedAt(null)
                .build();
        
        assertNull(teacher.getId());
        assertNull(teacher.getFirstName());
        assertNull(teacher.getLastName());
        assertNull(teacher.getCreatedAt());
        assertNull(teacher.getUpdatedAt());
    }
    
    @Test
    public void testEqualsSameObject() {
        Teacher teacher = new Teacher();
        assertEquals(teacher, teacher);
    }
    
    @Test
    public void testEqualsWithDifferentClass() {
        Teacher teacher = new Teacher();
        assertNotEquals(teacher, new Object());
    }
    
    @Test
    public void testEqualsWithNull() {
        Teacher teacher = new Teacher();
        assertNotEquals(teacher, null);
    }
    
    @Test
    public void testEqualsWithNulls() {
        Teacher teacher1 = new Teacher();
        teacher1.setId(1L);
        
        Teacher teacher2 = new Teacher();
        teacher2.setId(null);
        
        assertNotEquals(teacher1, teacher2);
        assertNotEquals(teacher2, teacher1);
    }
}