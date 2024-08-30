package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {

    Optional<Users> findByUsername(String username);

    // tìm kiếm hạn chế. cho người dùng tìm kiếm nhau 
    Optional<Users> findByEmail(String email);
    List<Users> findByPhoneNumber(String phoneNumber);
    // cho quản trị viên tìm
    List<Users> findByEmailContainingIgnoreCase(String email);
    List<Users> findByPhoneNumberContainingIgnoreCase(String phoneNumber);

    List<Users> findByNameContainingIgnoreCase(String name);
    
}
