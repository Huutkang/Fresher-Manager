package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.FresherProject;

@Repository
public interface FresherProjectRepository extends JpaRepository<FresherProject, Integer> {

    
}
