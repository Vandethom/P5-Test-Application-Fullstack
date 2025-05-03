package com.openclassrooms.starterjwt.payload.request;

import static org.junit.jupiter.api.Assertions.*;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.ConstraintViolation;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LoginRequestTest {

    private Validator validator;
    
    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    
    @Test
    public void testGetterAndSetterForEmail() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        String email = "test@example.com";
        
        // Act
        loginRequest.setEmail(email);
        
        // Assert
        assertEquals(email, loginRequest.getEmail());
    }
    
    @Test
    public void testGetterAndSetterForPassword() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        String password = "password123";
        
        // Act
        loginRequest.setPassword(password);
        
        // Assert
        assertEquals(password, loginRequest.getPassword());
    }
    
    @Test
    public void testEmailValidation_WhenEmailIsBlank() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("");
        loginRequest.setPassword("password123");
        
        // Act & Assert
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);
        assertEquals(1, violations.size());
        assertEquals("email", violations.iterator().next().getPropertyPath().toString());
    }
    
    @Test
    public void testPasswordValidation_WhenPasswordIsBlank() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("");
        
        // Act & Assert
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);
        assertEquals(1, violations.size());
        assertEquals("password", violations.iterator().next().getPropertyPath().toString());
    }
    
    @Test
    public void testValidation_WhenAllFieldsValid() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");
        
        // Act & Assert
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);
        assertEquals(0, violations.size());
    }

    @Test
    public void testEmailNullValidation() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword("password123");

        // Act
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

        // Assert
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    public void testPasswordNullValidation() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");

        // Act
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

        // Assert
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    public void testEqualsHashCodeAndToString() {
        // Arrange
        LoginRequest a = new LoginRequest();
        a.setEmail("user@example.com");
        a.setPassword("secret");
        LoginRequest b = new LoginRequest();
        b.setEmail("user@example.com");
        b.setPassword("secret");

        // Act & Assert
        // Default equals is identity, so distinct instances should not be equal
        assertNotEquals(a, b);
        String repr = a.toString();
        assertTrue(repr.startsWith(LoginRequest.class.getName() + "@"));
    }
}