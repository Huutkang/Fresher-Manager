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

    Optional<Fresher> findByCenterId(Integer centerId);

    List<Fresher> findByUser_PhoneNumber(String phoneNumber);
    Optional<Fresher> findByUser_Email(String email);

    List<Fresher> findByUser_NameContainingIgnoreCase(String name); //
    List<Fresher> findByUser_PhoneNumberContainingIgnoreCase(String phoneNumber);
    Optional<Fresher> findByUser_EmailContainingIgnoreCase(String email); //

    List<Fresher> findByProgrammingLanguageContainingIgnoreCase(String language); //
    List<Fresher> findByCenterNameContainingIgnoreCase(String name);
}

