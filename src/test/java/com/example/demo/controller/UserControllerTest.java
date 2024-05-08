package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;


    @Test
    public void testRegistrationSuccess() throws Exception {
        when(userService.registration(any(User.class))).thenReturn(true);
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"example@example.com\",\"password\":\"123456\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Registration complete!"));
    }

    @Test
    public void testRegistrationFail() throws Exception {
        when(userService.registration(any(User.class))).thenReturn(false);
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"example@example.com\",\"password\":\"123456\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("This email is already registered!"));
    }

    @Test
    public void testLogin() throws Exception {
        when(userService.login("example@example.com", "123456")).thenReturn("token123");
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"example@example.com\",\"password\":\"123456\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("token123"));
    }

    @Test
    public void testLogoutSuccess() throws Exception {
        when(userService.logout("\"token123\"")).thenReturn(true);
        mockMvc.perform(post("/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"token123\""))
                .andExpect(status().isOk())
                .andExpect(content().string("You successfully logged out"));
    }

    @Test
    public void testLogoutFail() throws Exception {
        when(userService.logout("token123")).thenReturn(false);
        mockMvc.perform(post("/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"token123\""))
                .andExpect(status().isOk())
                .andExpect(content().string("Invalid token"));
    }

    @Test
    public void testDeleteUserSuccess() throws Exception {
        when(userService.deleteUser("\"token123\"")).thenReturn(true);
        mockMvc.perform(delete("/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"token123\""))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted"));
    }

    @Test
    public void testDeleteUserFail() throws Exception {
        when(userService.deleteUser("\"token123\"")).thenReturn(false);
        mockMvc.perform(delete("/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"token123\""))
                .andExpect(status().isOk())
                .andExpect(content().string("Invalid token"));
    }
}

