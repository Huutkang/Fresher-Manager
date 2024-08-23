package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class FresherProject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_user"))
    private Users user;

    @ManyToOne
    @JoinColumn(name = "project_id", foreignKey = @ForeignKey(name = "fk_project"))
    private Project project;

    private String role;
}
