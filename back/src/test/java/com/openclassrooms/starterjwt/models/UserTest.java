package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    @Test
    public void testGettersSettersAndEquals() {
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("test@example.com");
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setPassword("pass");
        user1.setAdmin(true);
        assertEquals(1L, user1.getId());
        assertEquals("test@example.com", user1.getEmail());
        assertEquals("John", user1.getFirstName());
        assertEquals("Doe", user1.getLastName());
        assertEquals("pass", user1.getPassword());
        assertTrue(user1.isAdmin());
        User user2 = new User("test@example.com", "Doe", "John", "pass", true);
        user2.setId(1L);
        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
        assertTrue(user1.toString().contains("test@example.com"));
    }
    
    @Test
    public void testBuilder() {
        LocalDateTime now = LocalDateTime.now();
        User user = User.builder()
                .id(1L)
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("pass")
                .admin(true)
                .createdAt(now)
                .updatedAt(now)
                .build();
        
        assertEquals(1L, user.getId());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("pass", user.getPassword());
        assertTrue(user.isAdmin());
        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getUpdatedAt());
    }
    
    @Test
    public void testBuilderWithDefaults() {
        // Testing builder with default/minimal values, respecting @NonNull constraints
        User user = User.builder()
                .email("minimal@example.com") // Required
                .lastName("LastName")      // Required
                .firstName("FirstName")   // Required
                .password("password")      // Required
                .admin(false)                 // Required
                .build();
        
        assertNull(user.getId());        // Optional
        assertEquals("minimal@example.com", user.getEmail());
        assertEquals("LastName", user.getLastName());
        assertEquals("FirstName", user.getFirstName());
        assertEquals("password", user.getPassword());
        assertFalse(user.isAdmin());
        assertNull(user.getCreatedAt()); // Optional
        assertNull(user.getUpdatedAt()); // Optional
    }
    
    @Test
    public void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        User user = new User(1L, "test@example.com", "Doe", "John", "pass", true, now, now);
        
        assertEquals(1L, user.getId());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("pass", user.getPassword());
        assertTrue(user.isAdmin());
        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getUpdatedAt());
    }
    
    @Test
    public void testNoArgsConstructor() {
        User user = new User();
        
        assertNull(user.getId());
        assertNull(user.getEmail());
        assertNull(user.getFirstName());
        assertNull(user.getLastName());
        assertNull(user.getPassword());
        assertFalse(user.isAdmin());
        assertNull(user.getCreatedAt());
        assertNull(user.getUpdatedAt());
    }
    
    @Test
    public void testChaining() {
        User user = new User()
                .setId(1L)
                .setEmail("test@example.com")
                .setFirstName("John")
                .setLastName("Doe")
                .setPassword("pass")
                .setAdmin(true);
        
        assertEquals(1L, user.getId());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("pass", user.getPassword());
        assertTrue(user.isAdmin());
    }
    
    @Test
    public void testEqualsSameObject() {
        User user = new User();
        assertEquals(user, user);
    }
    
    @Test
    public void testEqualsWithDifferentClass() {
        User user = new User();
        assertNotEquals(user, new Object());
    }
    
    @Test
    public void testEqualsWithNull() {
        User user = new User();
        assertNotEquals(user, null);
    }
    
    @Test
    public void testEqualsWithNulls() {
        User user1 = new User();
        user1.setId(1L);
        
        User user2 = new User();
        user2.setId(null);
        
        assertNotEquals(user1, user2);
        assertNotEquals(user2, user1);
    }
    
    @Test
    public void testDifferentIds() {
        User user1 = new User();
        user1.setId(1L);
        
        User user2 = new User();
        user2.setId(2L);
        
        assertNotEquals(user1, user2);
    }
}
