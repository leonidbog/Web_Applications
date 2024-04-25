package com.example.demo.repository;

import com.example.demo.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepo extends JpaRepository<ShoppingCart, Long> {
    ShoppingCart findByUserEmail(String email);
}
