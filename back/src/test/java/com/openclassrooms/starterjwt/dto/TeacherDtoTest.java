package com.openclassrooms.starterjwt.dto;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class TeacherDtoTest {
    @Test
    void testNoArgsConstructorAndSettersGetters() {
        TeacherDto teacher = new TeacherDto();
        teacher.setId(1L);
        teacher.setLastName("Doe");
        teacher.setFirstName("John");
        LocalDateTime now = LocalDateTime.now();
        teacher.setCreatedAt(now);
        teacher.setUpdatedAt(now);

        assertEquals(1L, teacher.getId());
        assertEquals("Doe", teacher.getLastName());
        assertEquals("John", teacher.getFirstName());
        assertEquals(now, teacher.getCreatedAt());
        assertEquals(now, teacher.getUpdatedAt());
    }

    @Test
    void testAllArgsConstructorAndEqualsHashCode() {
        LocalDateTime now = LocalDateTime.now();
        TeacherDto t1 = new TeacherDto(1L, "Doe", "John", now, now);
        TeacherDto t2 = new TeacherDto(1L, "Doe", "John", now, now);
        TeacherDto t3 = new TeacherDto(2L, "Smith", "Jane", now, now);

        assertEquals(t1, t2);
        assertEquals(t1.hashCode(), t2.hashCode());
        assertNotEquals(t1, t3);
        assertNotEquals(t1.hashCode(), t3.hashCode());
    }

    @Test
    void testToString() {
        LocalDateTime now = LocalDateTime.now();
        TeacherDto teacher = new TeacherDto(1L, "Doe", "John", now, now);
        String str = teacher.toString();
        assertTrue(str.contains("Doe"));
        assertTrue(str.contains("John"));
    }
}
