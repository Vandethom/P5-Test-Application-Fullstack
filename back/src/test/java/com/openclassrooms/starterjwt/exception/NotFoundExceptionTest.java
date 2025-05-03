package com.openclassrooms.starterjwt.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.junit.jupiter.api.Assertions.*;

public class NotFoundExceptionTest {

    @Test
    public void testNotFoundExceptionIsRuntimeException() {
        NotFoundException exception = new NotFoundException();
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    public void testNotFoundExceptionHasCorrectResponseStatus() {
        ResponseStatus responseStatus = NotFoundException.class.getAnnotation(ResponseStatus.class);
        assertNotNull(responseStatus);
        assertEquals(HttpStatus.NOT_FOUND, responseStatus.value());
    }
}