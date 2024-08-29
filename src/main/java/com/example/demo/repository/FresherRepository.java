package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Fresher;
import com.example.demo.entity.Users;


@Repository
public interface FresherRepository extends JpaRepository<Fresher, Integer> {
    Optional<Fresher> findByUser(Users user);
    List<Fresher> findByCenterId(Integer centerId);
    List<Fresher> findByUser_NameContainingIgnoreCase(String name);
    List<Fresher> findByProgrammingLanguageContainingIgnoreCase(String username);
    List<Fresher> findByUser_EmailContainingIgnoreCase(String email);
}
