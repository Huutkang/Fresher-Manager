package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "center_id", foreignKey = @ForeignKey(name = "fk_center"))
    private Center center;

    @ManyToOne
    @JoinColumn(name = "manager_id", foreignKey = @ForeignKey(name = "fk_manager"))
    private Users manager;

    private String language;

    @Column(nullable = false)
    private String status;

    private java.sql.Date startDate;

    private java.sql.Date endDate;

    @Column(nullable = false)
    private boolean active = true;
}


