package com.example.demo.service;

import com.example.demo.model.Item;
import com.example.demo.model.Session;
import com.example.demo.model.ShoppingCart;
import com.example.demo.model.User;
import com.example.demo.repository.PostgreItemRepository;
import com.example.demo.repository.SessionRepository;
import com.example.demo.repository.ShoppingCartRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private ShoppingCartRepo shoppingCartRepo;

    @Mock
    private PostgreItemRepository itemRepository;

    @InjectMocks
    private ShoppingCartService shoppingCartService;

    @Test
    void addItem_ShouldAddItemToCart() {
        Long itemId = 1L;
        String token = "validToken";
        Item item = new Item();
        User user = new User();
        user.setEmail("test@example.com");
        Session session = new Session(token, user);
        ShoppingCart cart = new ShoppingCart();
        cart.setItems(new ArrayList<>());

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(sessionRepository.findById(token)).thenReturn(Optional.of(session));
        when(shoppingCartRepo.findByUserEmail(user.getEmail())).thenReturn(cart);

        assertTrue(shoppingCartService.addItem(itemId, token));
        assertTrue(cart.getItems().contains(item));
        verify(shoppingCartRepo).save(cart);
    }

    @Test
    void addItem_FailureWhenItemOrSessionNotFound() {
        Long itemId = 1L;
        String token = "invalidToken";

        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());
        when(sessionRepository.findById(token)).thenReturn(Optional.empty());

        assertFalse(shoppingCartService.addItem(itemId, token));
        verify(shoppingCartRepo, never()).save(any(ShoppingCart.class));
    }

    @Test
    void deleteItem_ShouldRemoveItemFromCart() {
        Long itemId = 1L;
        String token = "validToken";
        Item item = new Item();
        User user = new User();
        user.setEmail("test@example.com");
        Session session = new Session(token, user);
        ShoppingCart cart = new ShoppingCart();
        cart.setItems(new ArrayList<>(Collections.singletonList(item)));

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(sessionRepository.findById(token)).thenReturn(Optional.of(session));
        when(shoppingCartRepo.findByUserEmail(user.getEmail())).thenReturn(cart);

        assertTrue(shoppingCartService.deleteItem(itemId, token));
        assertFalse(cart.getItems().contains(item));
        verify(shoppingCartRepo).save(cart);
    }

    @Test
    void deleteItem_FailureWhenItemOrSessionNotFound() {
        Long itemId = 1L;
        String token = "invalidToken";

        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());
        when(sessionRepository.findById(token)).thenReturn(Optional.empty());

        assertFalse(shoppingCartService.deleteItem(itemId, token));
        verify(shoppingCartRepo, never()).save(any(ShoppingCart.class));
    }

    @Test
    void showCart_ShouldReturnItemsInCart() {
        String token = "validToken";
        User user = new User();
        user.setEmail("test@example.com");
        Session session = new Session(token, user);
        ShoppingCart cart = new ShoppingCart();
        List<Item> expectedItems = new ArrayList<>(Collections.singletonList(new Item()));
        cart.setItems(expectedItems);

        when(sessionRepository.findById(token)).thenReturn(Optional.of(session));
        when(shoppingCartRepo.findByUserEmail(user.getEmail())).thenReturn(cart);

        List<Item> items = shoppingCartService.showCart(token);
        assertEquals(expectedItems, items);
    }

    @Test
    void showCart_ShouldReturnEmptyListIfSessionNotFound() {
        String token = "invalidToken";

        when(sessionRepository.findById(token)).thenReturn(Optional.empty());

        List<Item> items = shoppingCartService.showCart(token);
        assertTrue(items.isEmpty());
    }
}
