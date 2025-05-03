package com.openclassrooms.starterjwt.dto;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

public class SessionDtoTest {
    @Test
    public void testGettersSettersAndEquals() {
        SessionDto dto1 = new SessionDto();
        dto1.setId(1L);
        dto1.setName("Session");
        dto1.setDate(new Date());
        dto1.setTeacher_id(2L);
        dto1.setDescription("desc");
        dto1.setUsers(Arrays.asList(1L, 2L));
        assertEquals(1L, dto1.getId());
        assertEquals("Session", dto1.getName());
        assertEquals(2L, dto1.getTeacher_id());
        assertEquals("desc", dto1.getDescription());
        assertEquals(Arrays.asList(1L, 2L), dto1.getUsers());
        SessionDto dto2 = new SessionDto(1L, "Session", dto1.getDate(), 2L, "desc", Arrays.asList(1L, 2L), null, null);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertTrue(dto1.toString().contains("Session"));
    }
}
