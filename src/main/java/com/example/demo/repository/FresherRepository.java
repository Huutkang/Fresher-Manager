package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Fresher;
import com.example.demo.entity.Users;

import java.util.Optional;


@Repository
public interface FresherRepository extends JpaRepository<Fresher, Integer> {
    Optional<Fresher> findByUser(Users user);
    List<Fresher> findByCenterId(Integer centerId);
}
