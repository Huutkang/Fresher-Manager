package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Fresher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_user"), nullable = false, unique = true)
    private Users user;

    private String programmingLanguage;

    @ManyToOne
    @JoinColumn(name = "center_id", foreignKey = @ForeignKey(name = "fk_center"), unique = true)
    private Center center;
    
    @Column(nullable = false)
    private boolean active = true;
}
