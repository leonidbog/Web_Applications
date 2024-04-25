package com.example.demo.service;

import com.example.demo.model.Session;
import com.example.demo.model.ShoppingCart;
import com.example.demo.model.User;
import com.example.demo.repository.PostgreUsersRepository;
import com.example.demo.repository.SessionRepository;
import com.example.demo.repository.ShoppingCartRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService {

    private PostgreUsersRepository usersRepository;
    private SessionRepository sessionRepository;
    private ShoppingCartRepo shoppingCartRepo;

    public boolean registration(User user) {
        if (usersRepository.existsById(user.getEmail())) {
            return false;
        }

        ShoppingCart cart = new ShoppingCart();
        cart.setUser(user);
        cart.setItems(new ArrayList<>());

        usersRepository.save(user);
        shoppingCartRepo.save(cart);
        return true;
    }


    public String login(String email, String password) {
        Optional<User> optionalUser = usersRepository.findById(email);
        if (optionalUser.isEmpty()) {
            return "no authorized";
        }
        User user = optionalUser.get();

        if (!user.getPassword().equals(password)) {
            return "no authorized";
        }

        String token = email + password + LocalDateTime.now();

        Session session = new Session(token, user);
        sessionRepository.save(session);

        return token;
    }

    public boolean logout(String token) {
        Optional<Session> optionalSession = sessionRepository.findById(token);
        if(optionalSession.isEmpty()) return false;

        Session session = optionalSession.get();
        sessionRepository.delete(session);
        return true;
    }

    public boolean deleteUser(String token) {
        Optional<Session> optionalSession = sessionRepository.findById(token);
        if(optionalSession.isEmpty()) return false;

        Session session = optionalSession.get();
        User user = session.getUser();
        usersRepository.delete(user);
        return true;
    }
}
