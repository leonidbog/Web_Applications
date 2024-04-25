package com.example.demo.controller;

import com.example.demo.model.Item;
import com.example.demo.service.PostgreItemService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class ItemController {
    private PostgreItemService shopService;

    @GetMapping("allIcecreams")
    public List<Item> findAllItems() {
        return shopService.findAllItems();
    }

    @GetMapping("Icecreams/{id}")
    public Item findItemById(@PathVariable Long id) {
        return shopService.findItemById(id);
    }

    @PostMapping("saveIcecreams")
    public Item saveItem(@RequestBody Item item) {
        return shopService.saveItem(item);
    }

    @PostMapping("editIcecreams")
    public Item updateItem(@RequestBody Item item) {
        return shopService.updateItem(item);
    }

    @DeleteMapping("deleteIcecreams/{id}")
    public void deleteItemById(@PathVariable Long id) {
        shopService.deleteItemById(id);
    }
}


