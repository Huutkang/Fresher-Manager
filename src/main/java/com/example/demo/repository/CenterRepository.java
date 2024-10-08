package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;   

import com.example.demo.entity.Center;   

@Repository
public interface CenterRepository extends JpaRepository <Center, Integer> {
    Optional<Center> findByManagerId(Integer managerId);

    List<Center> findByNameContainingIgnoreCase(String name);
    List<Center> findByLocationContainingIgnoreCase(String location);
    
}

