package com.example.demo.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;   

import com.example.demo.entity.Token;   

@Repository
public interface TokenRepository extends JpaRepository <Token, String> {

    // Tìm tất cả các token hết hạn
    List<Token> findByExpiryTimeBefore(Date now);
}

