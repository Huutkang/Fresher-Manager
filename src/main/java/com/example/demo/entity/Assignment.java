package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "fresher_id", foreignKey = @ForeignKey(name = "fk_fresher"), nullable = false)
    private Fresher fresher;

    @ManyToOne
    @JoinColumn(name = "project_id", foreignKey = @ForeignKey(name = "fk_project"), nullable = false)
    private Project project;

    @Column
    private int assignment_number;

    @Column
    private Double score;

    @PostLoad
    @PrePersist
    @PreUpdate
    private void validateConstraints() {
        if (assignment_number < 1 || assignment_number > 3) {
            throw new IllegalArgumentException("Assignment number must be between 1 and 3.");
        }
        if (score < 0 || score > 10) {
            throw new IllegalArgumentException("Score must be between 0 and 10.");
        }
    }
}
