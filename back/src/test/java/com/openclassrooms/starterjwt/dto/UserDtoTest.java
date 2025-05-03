package com.openclassrooms.starterjwt.dto;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class UserDtoTest {
    @Test
    void testNoArgsConstructorAndSettersGetters() {
        UserDto user = new UserDto();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setLastName("Doe");
        user.setFirstName("John");
        user.setAdmin(true);
        user.setPassword("secret");
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        assertEquals(1L, user.getId());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("Doe", user.getLastName());
        assertEquals("John", user.getFirstName());
        assertTrue(user.isAdmin());
        assertEquals("secret", user.getPassword());
        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getUpdatedAt());
    }

    @Test
    void testAllArgsConstructorAndEqualsHashCode() {
        LocalDateTime now = LocalDateTime.now();
        UserDto user1 = new UserDto(1L, "test@example.com", "Doe", "John", true, "secret", now, now);
        UserDto user2 = new UserDto(1L, "test@example.com", "Doe", "John", true, "secret", now, now);
        UserDto user3 = new UserDto(2L, "other@example.com", "Smith", "Jane", false, "pass", now, now);

        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
        assertNotEquals(user1, user3);
        assertNotEquals(user1.hashCode(), user3.hashCode());
    }

    @Test
    void testToString() {
        LocalDateTime now = LocalDateTime.now();
        UserDto user = new UserDto(1L, "test@example.com", "Doe", "John", true, "secret", now, now);
        String str = user.toString();
        assertTrue(str.contains("test@example.com"));
        assertTrue(str.contains("Doe"));
        assertTrue(str.contains("John"));
    }
}
