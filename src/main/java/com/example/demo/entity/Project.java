package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.ForeignKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 255)
    private String name;

    @ManyToOne
    @JoinColumn(name = "center_id", foreignKey = @ForeignKey(name = "fk_center"), nullable = true)
    private Center center;

    @Column(length = 255)
    private String manager_name;

    @Column
    private Date start_date;

    @Column
    private Date end_date;

    @Column(length = 100)
    private String language;

    @Column(length = 50)
    private String status;

    public void setStatus(String status) {
        if (status.equals("not start") || status.equals("ongoing") || status.equals("canceled") || status.equals("closed")) {
            this.status = status;
        } else {
            throw new IllegalArgumentException("Invalid status: " + status);
        }
    }
}
