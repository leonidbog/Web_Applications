package com.example.demo.controller;

import com.example.demo.model.Item;
import com.example.demo.service.PostgreItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
public class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostgreItemService shopService;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testFindAllItems() throws Exception {
        List<Item> items = Arrays.asList(new Item(1L, "Icecream 1", "BrandA", "Type1", 100),
                new Item(2L, "Icecream 2", "BrandB", "Type2", 150));
        String jsonItems = objectMapper.writeValueAsString(items);
        when(shopService.findAllItems()).thenReturn(items);

        mockMvc.perform(get("/all-icecreams"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonItems));
    }

    @Test
    public void testFindItemById() throws Exception {
        Long id = 1L;
        Item item = new Item(id, "Icecream 1", "BrandA", "Type1", 100);
        String jsonItem = objectMapper.writeValueAsString(item);
        when(shopService.findItemById(id)).thenReturn(item);

        mockMvc.perform(get("/Icecreams/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonItem));
    }

    @Test
    public void testSaveItem() throws Exception {
        Item item = new Item(1L, "Icecream 3", "BrandC", "Type3", 200);
        String jsonItem = objectMapper.writeValueAsString(item);
        when(shopService.saveItem(any(Item.class))).thenReturn(item);

        mockMvc.perform(post("/add-icecream")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonItem))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonItem));
    }

    @Test
    public void testUpdateItem() throws Exception {
        Item item = new Item(1L, "Updated Icecream", "BrandA", "Type1", 105);
        String jsonItem = objectMapper.writeValueAsString(item);
        when(shopService.updateItem(any(Item.class))).thenReturn(item);

        mockMvc.perform(post("/edit-icecreams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonItem))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonItem));
    }

    @Test
    public void testDeleteItemById() throws Exception {
        Long id = 1L;
        doNothing().when(shopService).deleteItemById(id);

        mockMvc.perform(delete("/delete-icecreams/{id}", id))
                .andExpect(status().isOk());
    }
}

