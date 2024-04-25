package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "carts")
public class ShoppingCart {
    @Id
    @GeneratedValue
    Long id;

    @OneToOne
    User user;

    @OneToMany
    List<Item> items;

}
