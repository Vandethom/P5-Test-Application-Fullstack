package com.openclassrooms.starterjwt.payload.request;

import static org.junit.jupiter.api.Assertions.*;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.ConstraintViolation;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SignupRequestTest {

    private Validator validator;
    
    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    
    private String repeatString(String str, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
    
    @Test
    public void testGetterAndSetterForEmail() {
        SignupRequest signupRequest = new SignupRequest();
        String email = "test@example.com";
        
        signupRequest.setEmail(email);
        
        assertEquals(email, signupRequest.getEmail());
    }
    
    @Test
    public void testGetterAndSetterForFirstName() {
        SignupRequest signupRequest = new SignupRequest();
        String firstName = "John";
        
        signupRequest.setFirstName(firstName);
        
        assertEquals(firstName, signupRequest.getFirstName());
    }
    
    @Test
    public void testGetterAndSetterForLastName() {
        SignupRequest signupRequest = new SignupRequest();
        String lastName = "Doe";
        
        signupRequest.setLastName(lastName);
        
        assertEquals(lastName, signupRequest.getLastName());
    }
    
    @Test
    public void testGetterAndSetterForPassword() {
        SignupRequest signupRequest = new SignupRequest();
        String password = "password123";
        
        signupRequest.setPassword(password);
        
        assertEquals(password, signupRequest.getPassword());
    }
    
    @Test
    public void testEmailValidation_WhenEmailIsBlank() {
        // Arrange
        SignupRequest signupRequest = createValidSignupRequest();
        signupRequest.setEmail("");
        
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertTrue(violations.size() >= 1);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }
    
    @Test
    public void testEmailValidation_WhenEmailIsInvalid() {
        SignupRequest signupRequest = createValidSignupRequest();
        signupRequest.setEmail("invalid-email");
        
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertTrue(violations.size() >= 1);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }
    
    @Test
    public void testEmailValidation_WhenEmailIsTooLong() {
        SignupRequest signupRequest = createValidSignupRequest();
        signupRequest.setEmail(repeatString("a", 45) + "@example.com"); // 51 characters
        
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertTrue(violations.size() >= 1);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }
    
    @Test
    public void testFirstNameValidation_WhenFirstNameIsBlank() {
        SignupRequest signupRequest = createValidSignupRequest();
        signupRequest.setFirstName("");
        
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertTrue(violations.size() >= 1);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("firstName")));
    }
    
    @Test
    public void testFirstNameValidation_WhenFirstNameIsTooShort() {
        SignupRequest signupRequest = createValidSignupRequest();
        signupRequest.setFirstName("Jo"); // Less than 3 characters
        
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertTrue(violations.size() >= 1);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("firstName")));
    }
    
    @Test
    public void testFirstNameValidation_WhenFirstNameIsTooLong() {
        SignupRequest signupRequest = createValidSignupRequest();
        signupRequest.setFirstName(repeatString("J", 21)); // 21 characters
        
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertTrue(violations.size() >= 1);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("firstName")));
    }
    
    @Test
    public void testLastNameValidation_WhenLastNameIsBlank() {
        SignupRequest signupRequest = createValidSignupRequest();
        signupRequest.setLastName("");
        
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertTrue(violations.size() >= 1);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("lastName")));
    }
    
    @Test
    public void testLastNameValidation_WhenLastNameIsTooShort() {
        SignupRequest signupRequest = createValidSignupRequest();
        signupRequest.setLastName("Do"); // Less than 3 characters
        
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertTrue(violations.size() >= 1);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("lastName")));
    }
    
    @Test
    public void testLastNameValidation_WhenLastNameIsTooLong() {
        SignupRequest signupRequest = createValidSignupRequest();
        signupRequest.setLastName(repeatString("D", 21)); // 21 characters
        
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertTrue(violations.size() >= 1);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("lastName")));
    }
    
    @Test
    public void testPasswordValidation_WhenPasswordIsBlank() {
        SignupRequest signupRequest = createValidSignupRequest();
        signupRequest.setPassword("");
        
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertTrue(violations.size() >= 1);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }
    
    @Test
    public void testPasswordValidation_WhenPasswordIsTooShort() {
        SignupRequest signupRequest = createValidSignupRequest();
        signupRequest.setPassword("pass"); // Less than 6 characters
        
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertTrue(violations.size() >= 1);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }
    
    @Test
    public void testPasswordValidation_WhenPasswordIsTooLong() {
        SignupRequest signupRequest = createValidSignupRequest();
        signupRequest.setPassword(repeatString("p", 41)); // 41 characters
        
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertTrue(violations.size() >= 1);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }
    
    @Test
    public void testValidation_WhenAllFieldsValid() {
        SignupRequest signupRequest = createValidSignupRequest();
        
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertEquals(0, violations.size());
    }
    
    private SignupRequest createValidSignupRequest() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("password123");
        return signupRequest;
    }

    @Test
    public void testEmailNullValidation() {
        SignupRequest signupRequest = createValidSignupRequest();
        signupRequest.setEmail(null);

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    public void testFirstNameNullValidation() {
        SignupRequest signupRequest = createValidSignupRequest();
        signupRequest.setFirstName(null);

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("firstName")));
    }

    @Test
    public void testLastNameNullValidation() {
        SignupRequest signupRequest = createValidSignupRequest();
        signupRequest.setLastName(null);

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("lastName")));
    }

    @Test
    public void testPasswordNullValidation() {
        SignupRequest signupRequest = createValidSignupRequest();
        signupRequest.setPassword(null);

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    public void testEqualsHashCodeAndToString() {
        SignupRequest a = createValidSignupRequest();
        SignupRequest b = createValidSignupRequest();

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        String repr = a.toString();
        assertTrue(repr.contains("test@example.com"));
        assertTrue(repr.contains("John"));
        assertTrue(repr.contains("Doe"));
        assertTrue(repr.contains("password123"));
    }
}