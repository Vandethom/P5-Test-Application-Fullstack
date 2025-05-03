package com.openclassrooms.starterjwt.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.junit.jupiter.api.Assertions.*;

public class BadRequestExceptionTest {

    @Test
    public void testBadRequestExceptionIsRuntimeException() {
        BadRequestException exception = new BadRequestException();
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    public void testBadRequestExceptionHasCorrectResponseStatus() {
        ResponseStatus responseStatus = BadRequestException.class.getAnnotation(ResponseStatus.class);
        assertNotNull(responseStatus);
        assertEquals(HttpStatus.BAD_REQUEST, responseStatus.value());
    }
}