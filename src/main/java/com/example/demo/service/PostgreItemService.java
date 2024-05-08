package com.example.demo.service;

import com.example.demo.model.Item;
import com.example.demo.repository.PostgreItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PostgreItemService {
    private PostgreItemRepository repository;

    public List<Item> findAllItems() {
        return repository.findAll();
    }

    public Item findItemById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Item saveItem(Item item) {
        return repository.save(item);
    }

    public Item updateItem(Item item) {
        return repository.save(item);
    }

    public void deleteItemById(Long id) {
        repository.deleteById(id);
    }
}
