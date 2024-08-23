package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Date;


@Entity
@Data
@NoArgsConstructor
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

    //  "not start", "ongoing", "canceled", hoặc "closed".
    private String status="not start";

    private Date startDate;

    private Date endDate;

    @Column(nullable = false)
    private boolean active = true;
}


