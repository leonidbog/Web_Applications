package com.example.demo.repository;

import com.example.demo.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostgreItemRepository extends JpaRepository<Item, Long> {
}
