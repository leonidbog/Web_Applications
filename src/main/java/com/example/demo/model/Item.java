package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Entity
@Table(name = "Ice creams")
public class Item {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String brand;
    private String type;
    private double price;

}
