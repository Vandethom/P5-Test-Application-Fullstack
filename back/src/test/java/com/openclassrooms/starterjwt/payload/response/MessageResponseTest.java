package com.openclassrooms.starterjwt.payload.response;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class MessageResponseTest {

    @Test
    public void testConstructor() {
        // Arrange
        String message = "Test message";
        
        // Act
        MessageResponse response = new MessageResponse(message);
        
        // Assert
        assertEquals(message, response.getMessage());
    }
    
    @Test
    public void testSetterAndGetter() {
        // Arrange
        MessageResponse response = new MessageResponse("Initial message");
        String newMessage = "New message";
        
        // Act
        response.setMessage(newMessage);
        
        // Assert
        assertEquals(newMessage, response.getMessage());
    }
}