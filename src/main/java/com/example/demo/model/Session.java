package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sessions")
public class Session {
    @Id
    @Column(nullable = false)
    String token;

    @JoinColumn(name = "user_email")
    @ManyToOne
    User user;
}
