package com.openclassrooms.starterjwt.security.services;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserDetailsImplTest {
    
    @Test
    public void testGetters() {
        // Given
        Long id = 1L;
        String username = "user@email.com";
        String firstName = "John";
        String lastName = "Doe";
        Boolean admin = false;
        String password = "password";
        
        // When
        UserDetailsImpl userDetails = new UserDetailsImpl(id, username, firstName, lastName, admin, password);
        
        // Then
        assertEquals(id, userDetails.getId());
        assertEquals(username, userDetails.getUsername());
        assertEquals(firstName, userDetails.getFirstName());
        assertEquals(lastName, userDetails.getLastName());
        assertEquals(admin, userDetails.getAdmin());
        assertEquals(password, userDetails.getPassword());
    }
    
    @Test
    public void testEqualsAndHashCode() {
        // Given
        UserDetailsImpl user1 = new UserDetailsImpl(1L, "user1@email.com", "John", "Doe", false, "pass");
        UserDetailsImpl user2 = new UserDetailsImpl(1L, "user2@email.com", "Jane", "Smith", true, "word");
        UserDetailsImpl user3 = new UserDetailsImpl(2L, "user1@email.com", "John", "Doe", false, "pass");
        UserDetailsImpl user1Copy = new UserDetailsImpl(1L, "user1@email.com", "John", "Doe", false, "pass");
        
        // Then
        assertEquals(user1, user1); // Same object
        assertEquals(user1, user2); // Same ID
        assertEquals(user1, user1Copy); // Same values
        assertNotEquals(user1, user3); // Different ID
        assertNotEquals(user1, null); // Null comparison
        assertNotEquals(user1, "Not a user"); // Different type
    }
    
    @Test
    public void testAuthoritiesAndSecurityMethods() {
        // Given
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "user@email.com", "John", "Doe", false, "pass");
        
        // Then
        assertTrue(userDetails.getAuthorities().isEmpty());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isEnabled());
    }
}
