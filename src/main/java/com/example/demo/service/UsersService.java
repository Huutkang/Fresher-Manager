package com.example.demo.service;

import com.example.demo.entity.Users;
import com.example.demo.repository.UsersRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;
    
    private PasswordEncoder Encoder = new BCryptPasswordEncoder(10);

    // Thêm mới User
    public Users addUser(Users user) {
        user.setPassword_hash(passwordEncoder(user.getPassword()));  
        return usersRepository.save(user);
    }

    // Lấy tất cả Users
    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    // Lấy User theo ID
    public Optional<Users> getUserById(int id) {
        return usersRepository.findById(id);
    }

    // Cập nhật User
    public Users updateUsername(int id, String username) {
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setUsername(username);
        return usersRepository.save(user);
    }

    public Users updatePassword(int id, String password) {
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setPassword_hash(passwordEncoder(password));
        return usersRepository.save(user);
    }
    
    public Users updateUser(int id, Users userDetails) {
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail());
        user.setPhoneNumber(userDetails.getPhoneNumber());
        user.setRole(userDetails.getRole());
        return usersRepository.save(user);
    }

    // Xóa User theo ID
    public void deleteUser(int id) {
        usersRepository.deleteById(id);
    }

    private String passwordEncoder(String password){
        return Encoder.encode(password);
    }
}
