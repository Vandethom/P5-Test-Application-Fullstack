package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserControllerTest {
    @Mock
    UserService userService;
    @Mock
    UserMapper userMapper;
    @InjectMocks
    UserController userController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetUserInfo() {
        User user = new User();
        user.setId(1L);
        user.setEmail("user@email.com");
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("user@email.com");
        when(userService.findById(1L)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);
        ResponseEntity<?> response = userController.findById("1");
        assertEquals(200, response.getStatusCodeValue());
        UserDto body = (UserDto) response.getBody();
        assertNotNull(body);
        assertEquals("user@email.com", body.getEmail());
    }

    @Test
    public void testFindByIdNotFound() {
        when(userService.findById(2L)).thenReturn(null);
        ResponseEntity<?> response = userController.findById("2");
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void testFindByIdBadRequest() {
        ResponseEntity<?> response = userController.findById("notANumber");
        assertEquals(400, response.getStatusCodeValue());
    }
}
