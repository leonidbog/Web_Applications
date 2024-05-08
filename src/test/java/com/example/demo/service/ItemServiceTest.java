package com.example.demo.service;

import com.example.demo.model.Item;
import com.example.demo.repository.PostgreItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {
    @InjectMocks
    PostgreItemService service;

    @Mock
    PostgreItemRepository repository;


    @Test
    void findItemById_exists(){
        Item item = new Item(1L, "name", "brand", "type", 123);
        when(repository.findById(1L)).thenReturn(Optional.of(item));

        Item itemById = service.findItemById(1L);

        assertEquals(item, itemById);
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void findItemById_not_found(){
        when(repository.findById(1L)).thenReturn(Optional.empty());

        Item itemById = service.findItemById(1L);

        assertNull(itemById);
        verify(repository, times(1)).findById(1L);
    }

        @Test
        public void testFindAllItems() {
            List<Item> expectedItems = Arrays.asList(new Item(), new Item());
            when(repository.findAll()).thenReturn(expectedItems);

            List<Item> actualItems = service.findAllItems();

            assertNotNull(actualItems);
            assertEquals(expectedItems.size(), actualItems.size());
            assertEquals(expectedItems, actualItems);
        }

        @Test
        public void testSaveItem() {
            Item item = new Item();
            when(repository.save(item)).thenReturn(item);

            Item savedItem = service.saveItem(item);

            assertNotNull(savedItem);
            assertEquals(item, savedItem);
        }

        @Test
        public void testUpdateItem() {
            Item item = new Item();
            when(repository.save(item)).thenReturn(item);

            Item updatedItem = service.updateItem(item);

            assertNotNull(updatedItem);
            assertEquals(item, updatedItem);
        }

        @Test
        public void testDeleteItemById() {
            Long id = 1L;

            doNothing().when(repository).deleteById(id);

            service.deleteItemById(id);

            verify(repository, times(1)).deleteById(id);
        }



}
