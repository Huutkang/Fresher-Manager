package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Center {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    private String location;

    @ManyToOne
    @JoinColumn(name = "manager_id", foreignKey = @ForeignKey(name = "fk_manager"))
    private Users manager;

    @Column(nullable = false)
    private boolean active = true;
}


