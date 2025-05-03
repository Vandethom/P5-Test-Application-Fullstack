package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SessionService sessionService;

    private Session testSession;
    private User testUser;

    @BeforeEach
    public void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setLastName("Test");
        testUser.setFirstName("User");

        testSession = new Session();
        testSession.setId(1L);
        testSession.setName("Test Session");
        testSession.setDescription("Test Description");
        testSession.setUsers(new ArrayList<>());
    }

    @Test
    public void testCreateSession() {
        when(sessionRepository.save(any(Session.class))).thenReturn(testSession);
        
        Session created = sessionService.create(testSession);
        
        assertNotNull(created);
        assertEquals("Test Session", created.getName());
        verify(sessionRepository).save(testSession);
    }

    @Test
    public void testDeleteSession() {
        doNothing().when(sessionRepository).deleteById(1L);
        
        sessionService.delete(1L);
        
        verify(sessionRepository).deleteById(1L);
    }

    @Test
    public void testFindAllSessions() {
        List<Session> sessions = Arrays.asList(testSession);
        when(sessionRepository.findAll()).thenReturn(sessions);
        
        List<Session> found = sessionService.findAll();
        
        assertNotNull(found);
        assertEquals(1, found.size());
        assertEquals("Test Session", found.get(0).getName());
    }

    @Test
    public void testGetSessionById() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(testSession));
        
        Session found = sessionService.getById(1L);
        
        assertNotNull(found);
        assertEquals(1L, found.getId());
    }

    @Test
    public void testGetSessionByIdNotFound() {
        when(sessionRepository.findById(2L)).thenReturn(Optional.empty());
        
        Session found = sessionService.getById(2L);
        
        assertNull(found);
    }

    @Test
    public void testUpdateSession() {
        when(sessionRepository.save(any(Session.class))).thenReturn(testSession);
        
        Session updated = sessionService.update(1L, testSession);
        
        assertNotNull(updated);
        assertEquals(1L, updated.getId());
        verify(sessionRepository).save(testSession);
    }

    @Test
    public void testParticipate() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(testSession));
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(sessionRepository.save(any(Session.class))).thenReturn(testSession);
        
        sessionService.participate(1L, 1L);
        
        verify(sessionRepository).save(testSession);
        assertTrue(testSession.getUsers().contains(testUser));
    }

    @Test
    public void testNoLongerParticipate() {
        testSession.getUsers().add(testUser);

        when(sessionRepository.findById(1L)).thenReturn(Optional.of(testSession));

        sessionService.noLongerParticipate(1L, 1L);

        verify(sessionRepository).save(testSession);
        assertFalse(testSession.getUsers().contains(testUser));
    }
}