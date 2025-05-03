package com.openclassrooms.starterjwt.payload.response;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class MessageResponseTest {

    @Test
    public void testConstructor() {
        String message = "Test message";
        
        MessageResponse response = new MessageResponse(message);
        
        assertEquals(message, response.getMessage());
    }
    
    @Test
    public void testSetterAndGetter() {
        MessageResponse response = new MessageResponse("Initial message");
        String newMessage = "New message";
        
        response.setMessage(newMessage);
        
        assertEquals(newMessage, response.getMessage());
    }
}