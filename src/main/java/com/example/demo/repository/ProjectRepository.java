package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {
    List<Project> findByNameContainingIgnoreCase(String name);

    List<Project> findByCenterId(Integer centerId);
    List<Project> findByManagerId(Integer managerId);
    List<Project> findByCenterIdAndManagerId(Integer centerId, Integer managerId);
    
    List<Project> findByLanguage(String language);
}
