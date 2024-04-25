package com.example.demo.service;

import com.example.demo.model.Item;
import com.example.demo.model.Session;
import com.example.demo.model.ShoppingCart;
import com.example.demo.model.User;
import com.example.demo.repository.PostgreItemRepository;
import com.example.demo.repository.SessionRepository;
import com.example.demo.repository.ShoppingCartRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ShoppingCartService {
    private SessionRepository sessionRepository;
    private ShoppingCartRepo shoppingCartRepo;
    private PostgreItemRepository itemRepository;

    public boolean addItem(Long itemId, String token) {
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        Optional<Session> optionalSession = sessionRepository.findById(token);
        if (optionalSession.isEmpty() | optionalItem.isEmpty()) {
            return false;
        }
        Session session = optionalSession.get();
        User user = session.getUser();
        Item item = optionalItem.get();

        ShoppingCart cart = shoppingCartRepo.findByUserEmail(user.getEmail());
        cart.getItems().add(item);

        shoppingCartRepo.save(cart);
        return true;
    }

    public boolean deleteItem(Long itemId, String token) {
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        Optional<Session> optionalSession = sessionRepository.findById(token);
        if (optionalSession.isEmpty() | optionalItem.isEmpty()) {
            return false;
        }
        Session session = optionalSession.get();
        User user = session.getUser();
        Item item = optionalItem.get();

        ShoppingCart cart = shoppingCartRepo.findByUserEmail(user.getEmail());
        cart.getItems().remove(item);

        shoppingCartRepo.save(cart);
        return true;
    }


    public List<Item> showCart(String token) {
        Optional<Session> optionalSession = sessionRepository.findById(token);
        if (optionalSession.isEmpty()) {
            return Collections.emptyList();
        }
        Session session = optionalSession.get();
        User user = session.getUser();
        ShoppingCart cart = shoppingCartRepo.findByUserEmail(user.getEmail());
        return cart.getItems();
    }
}
