package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;
    private User testUser;

    @BeforeEach
    public void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setLastName("Test");
        testUser.setFirstName("User");
        testUser.setPassword("password");
        testUser.setAdmin(false);
    }

    @Test
    public void testFindByIdSuccess() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        User found = userService.findById(1L);
        assertNotNull(found);
        assertEquals(1L, found.getId());
    }

    @Test
    public void testFindByIdNotFound() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        User found = userService.findById(2L);
        assertNull(found);
    }

    @Test
    public void testDeleteUserSuccess() {
        doNothing().when(userRepository).deleteById(1L);
        userService.delete(1L);
        verify(userRepository).deleteById(1L);
    }

    @Test
    public void testDeleteUserNotFound() {
        doThrow(new RuntimeException("User not found")).when(userRepository).deleteById(2L);
        assertThrows(RuntimeException.class, () -> userService.delete(2L));
    }
}
