package com.example.demo.entity;

import java.util.Set;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String username;
    
    @Column(nullable = false)
    private String password_hash;

    @Column(nullable = false)
    private String name;

    @Column(unique = true)
    private String email;
    
    @Column(length = 10)
    private String phoneNumber;

    private Set<String> roles;

    @Column(nullable = false)
    private boolean active = true;
}


