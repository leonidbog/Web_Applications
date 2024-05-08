package com.example.demo.service;

import com.example.demo.model.Session;
import com.example.demo.model.ShoppingCart;
import com.example.demo.model.User;
import com.example.demo.repository.PostgreUsersRepository;
import com.example.demo.repository.SessionRepository;
import com.example.demo.repository.ShoppingCartRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private PostgreUsersRepository usersRepository;

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private ShoppingCartRepo shoppingCartRepo;

    @InjectMocks
    private UserService userService;


    @Test
    public void testRegistration_UserExists() {
        User user = new User("email@example.com", "password123");
        when(usersRepository.existsById(user.getEmail())).thenReturn(true);

        boolean result = userService.registration(user);

        assertFalse(result);
        verify(usersRepository, never()).save(any(User.class));
    }

    @Test
    public void testRegistration_Success() {
        User user = new User("email@example.com", "password123");
        when(usersRepository.existsById(user.getEmail())).thenReturn(false);

        boolean result = userService.registration(user);

        assertTrue(result);
        verify(usersRepository).save(user);
        verify(shoppingCartRepo).save(any(ShoppingCart.class));
    }

    @Test
    public void testLogin_UserNotFound() {
        String email = "email@example.com";
        when(usersRepository.findById(email)).thenReturn(Optional.empty());

        String result = userService.login(email, "password123");

        assertEquals("no authorized", result);
    }

    @Test
    public void testLogin_IncorrectPassword() {
        User user = new User("email@example.com", "password123");
        when(usersRepository.findById(user.getEmail())).thenReturn(Optional.of(user));

        String result = userService.login(user.getEmail(), "wrongpassword");

        assertEquals("no authorized", result);
    }

    @Test
    public void testLogin_Success() {
        User user = new User("email@example.com", "password123");
        when(usersRepository.findById(user.getEmail())).thenReturn(Optional.of(user));

        String result = userService.login(user.getEmail(), user.getPassword());

        assertNotNull(result);
        verify(sessionRepository).save(any(Session.class));
    }

    @Test
    public void testLogout_SessionNotFound() {
        String token = "fakeToken";
        when(sessionRepository.findById(token)).thenReturn(Optional.empty());

        boolean result = userService.logout(token);

        assertFalse(result);
    }

    @Test
    public void testLogout_Success() {
        Session session = new Session("validToken", new User("email@example.com", "password123"));
        when(sessionRepository.findById(session.getToken())).thenReturn(Optional.of(session));

        boolean result = userService.logout(session.getToken());

        assertTrue(result);
        verify(sessionRepository).delete(session);
    }

    @Test
    public void testDeleteUser_SessionNotFound() {
        String token = "fakeToken";
        when(sessionRepository.findById(token)).thenReturn(Optional.empty());

        boolean result = userService.deleteUser(token);

        assertFalse(result);
    }

    @Test
    public void testDeleteUser_Success() {
        User user = new User("email@example.com", "password123");
        Session session = new Session("validToken", user);
        when(sessionRepository.findById(session.getToken())).thenReturn(Optional.of(session));

        boolean result = userService.deleteUser(session.getToken());

        assertTrue(result);
        verify(usersRepository).delete(user);
    }
}
