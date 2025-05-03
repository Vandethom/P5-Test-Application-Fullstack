package com.openclassrooms.starterjwt.security.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private User testUser;
    private final String testEmail = "test@example.com";

    @BeforeEach
    public void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail(testEmail);
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setPassword("password");
        testUser.setAdmin(false);
    }

    @Test
    public void loadUserByUsername_UserExists_ReturnsUserDetails() {
        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.of(testUser));

        UserDetails userDetails = userDetailsService.loadUserByUsername(testEmail);

        assertNotNull(userDetails);
        assertEquals(testEmail, userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertTrue(userDetails instanceof UserDetailsImpl);
        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) userDetails;
        assertEquals(1L, userDetailsImpl.getId());
        assertEquals("John", userDetailsImpl.getFirstName());
        assertEquals("Doe", userDetailsImpl.getLastName());
        verify(userRepository, times(1)).findByEmail(testEmail);
    }

    @Test
    public void loadUserByUsername_UserDoesNotExist_ThrowsUsernameNotFoundException() {
        String nonExistentEmail = "nonexistent@example.com";
        when(userRepository.findByEmail(nonExistentEmail)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(nonExistentEmail);
        });

        // Verify the exception message
        assertTrue(exception.getMessage().contains(nonExistentEmail));
        verify(userRepository, times(1)).findByEmail(nonExistentEmail);
    }

    @Test
    public void constructor_InitializesUserRepository() {
        UserRepository mockRepo = mock(UserRepository.class);
        
        UserDetailsServiceImpl service = new UserDetailsServiceImpl(mockRepo);
        
        assertNotNull(service);
        // We can't directly test the private field, but we can verify it was used correctly
        when(mockRepo.findByEmail(anyString())).thenReturn(Optional.of(testUser));
        UserDetails result = service.loadUserByUsername("any@email.com");
        assertNotNull(result);
        verify(mockRepo).findByEmail(anyString());
    }
}