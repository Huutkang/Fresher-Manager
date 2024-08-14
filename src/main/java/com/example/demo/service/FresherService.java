package com.example.demo.service;

import com.example.demo.entity.Fresher;
import com.example.demo.repository.FresherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FresherService {

    @Autowired
    private FresherRepository fresherRepository;

    // Thêm mới Fresher
    public Fresher addFresher(Fresher fresher) {
        return fresherRepository.save(fresher);
    }

    // Lấy tất cả Fresher
    public List<Fresher> getAllFreshers() {
        return fresherRepository.findAll();
    }

    // Lấy Fresher theo ID
    public Optional<Fresher> getFresherById(int id) {
        return fresherRepository.findById(id);
    }

    // Cập nhật Fresher
    public Fresher updateFresher(int id, Fresher fresherDetails) {
        Fresher fresher = fresherRepository.findById(id).orElseThrow(() -> new RuntimeException("Fresher not found"));
        fresher.setName(fresherDetails.getName());
        fresher.setEmail(fresherDetails.getEmail());
        fresher.setPhoneNumber(fresherDetails.getPhoneNumber());
        fresher.setProgrammingLanguage(fresherDetails.getProgrammingLanguage());
        fresher.setCenter(fresherDetails.getCenter());
        return fresherRepository.save(fresher);
    }

    // Xóa Fresher theo ID
    public void deleteFresher(int id) {
        fresherRepository.deleteById(id);
    }
}

