package com.example.demo.service;

import com.example.demo.entity.Center;
import com.example.demo.repository.CenterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CenterService {

    @Autowired
    private CenterRepository centerRepository;

    // Thêm mới Center
    public Center addCenter(Center center) {
        return centerRepository.save(center);
    }

    // Lấy tất cả Centers
    public List<Center> getAllCenters() {
        return centerRepository.findAll();
    }

    // Lấy Center theo ID
    public Optional<Center> getCenterById(int id) {
        return centerRepository.findById(id);
    }

    // Cập nhật Center
    public Center updateCenter(int id, Center centerDetails) {
        Center center = centerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Center not found"));
        center.setName(centerDetails.getName());
        center.setLocation(centerDetails.getLocation());
        center.setManager(centerDetails.getManager());
        return centerRepository.save(center);
    }

    // Xóa Center theo ID
    public Center deleteCenter(int id) {
        Center center = centersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        center.setActive(false);
        return centersRepository.save(center);
    }
}
