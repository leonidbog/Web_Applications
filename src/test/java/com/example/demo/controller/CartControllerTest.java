package com.example.demo.controller;

import com.example.demo.model.Item;
import com.example.demo.service.ShoppingCartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartController.class)
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShoppingCartService cartService;

    @Test
    public void addItem_ShouldReturnSuccessMessage() throws Exception {
        when(cartService.addItem(anyLong(), anyString())).thenReturn(true);

        mockMvc.perform(post("/cart/add")
                        .param("itemId", "1")
                        .param("token", "validToken"))
                .andExpect(status().isOk())
                .andExpect(content().string("Icecream added"));
    }

    @Test
    public void addItem_ShouldReturnFailureMessage() throws Exception {
        when(cartService.addItem(anyLong(), anyString())).thenReturn(false);

        mockMvc.perform(post("/cart/add")
                        .param("itemId", "1")
                        .param("token", "invalidToken"))
                .andExpect(status().isOk())
                .andExpect(content().string("Invalid token"));
    }

    @Test
    public void removeItem_ShouldReturnSuccessMessage() throws Exception {
        when(cartService.deleteItem(anyLong(), anyString())).thenReturn(true);

        mockMvc.perform(delete("/cart/remove")
                        .param("itemId", "1")
                        .param("token", "validToken"))
                .andExpect(status().isOk())
                .andExpect(content().string("Icecream deleted"));
    }

    @Test
    public void removeItem_ShouldReturnFailureMessage() throws Exception {
        when(cartService.deleteItem(anyLong(), anyString())).thenReturn(false);

        mockMvc.perform(delete("/cart/remove")
                        .param("itemId", "1")
                        .param("token", "invalidToken"))
                .andExpect(status().isOk())
                .andExpect(content().string("Invalid token"));
    }

    @Test
    public void showCart_ShouldReturnItems() throws Exception {
        Item item1 = new Item();
        item1.setName("Chocolate");
        Item item2 = new Item();
        item2.setName("Vanilla");

        when(cartService.showCart("validToken")).thenReturn(Arrays.asList(item1, item2));

        mockMvc.perform(get("/cart")
                        .param("token", "validToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Chocolate"))
                .andExpect(jsonPath("$[1].name").value("Vanilla"));
    }

    @Test
    public void showCart_ShouldReturnEmptyList() throws Exception {
        when(cartService.showCart("invalidToken")).thenReturn(Arrays.asList());

        mockMvc.perform(get("/cart")
                        .param("token", "invalidToken"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]")); // Expecting empty JSON array
    }
}
