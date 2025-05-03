package com.openclassrooms.starterjwt.payload.response;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class JwtResponseTest {

    @Test
    public void testConstructor() {
        // Arrange
        String accessToken = "test-token-123";
        Long id = 1L;
        String username = "test@example.com";
        String firstName = "John";
        String lastName = "Doe";
        Boolean admin = true;
        
        // Act
        JwtResponse response = new JwtResponse(accessToken, id, username, firstName, lastName, admin);
        
        // Assert
        assertEquals(accessToken, response.getToken());
        assertEquals("Bearer", response.getType()); // Default value
        assertEquals(id, response.getId());
        assertEquals(username, response.getUsername());
        assertEquals(firstName, response.getFirstName());
        assertEquals(lastName, response.getLastName());
        assertEquals(admin, response.getAdmin());
    }
    
    @Test
    public void testSettersAndGetters() {
        // Arrange
        JwtResponse response = new JwtResponse("initial-token", 1L, "test@example.com", "John", "Doe", false);
        
        String newToken = "new-token-456";
        String newType = "NewType";
        Long newId = 2L;
        String newUsername = "new@example.com";
        String newFirstName = "Jane";
        String newLastName = "Smith";
        Boolean newAdmin = true;
        
        // Act
        response.setToken(newToken);
        response.setType(newType);
        response.setId(newId);
        response.setUsername(newUsername);
        response.setFirstName(newFirstName);
        response.setLastName(newLastName);
        response.setAdmin(newAdmin);
        
        // Assert
        assertEquals(newToken, response.getToken());
        assertEquals(newType, response.getType());
        assertEquals(newId, response.getId());
        assertEquals(newUsername, response.getUsername());
        assertEquals(newFirstName, response.getFirstName());
        assertEquals(newLastName, response.getLastName());
        assertEquals(newAdmin, response.getAdmin());
    }
}