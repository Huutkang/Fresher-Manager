package com.example.demo.service;

import com.example.demo.entity.Fresher;
import com.example.demo.repository.FresherRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.example.demo.dto.internal.FresherDto;
import com.example.demo.dto.request.FresherReqDto;
import com.example.demo.entity.Users;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;

@Service
public class FresherService {

    @Autowired
    private FresherRepository fresherRepository;

    @Autowired
    UsersService usersService;

    public Fresher addFresher(Users user) {
        try{
            Fresher fresher = new Fresher();
            fresher.setUser(user);
            return fresherRepository.save(fresher);
        }catch (RuntimeException e) {
            throw new AppException(ErrorCode.ENTER_MISS_INFO);
        }
    }

    public Fresher addFresher(Users user, FresherDto fresherDto) {
        try{
            Fresher fresher = new Fresher();
            fresher.setUser(user);
            fresher.setProgrammingLanguage(fresherDto.getProgrammingLanguage());
            fresher.setCenter(fresherDto.getCenter());
            return fresherRepository.save(fresher);
        }catch (RuntimeException e) {
            throw new AppException(ErrorCode.ENTER_MISS_INFO);
        }
    }

    public Fresher addFresher(FresherReqDto fresherReqDto) {
        
        try{
            Users user = usersService.addUser(fresherReqDto.getUsername(), fresherReqDto.getPassword(), fresherReqDto.getName(), fresherReqDto.getEmail(), fresherReqDto.getPhoneNumber());
            Fresher fresher = new Fresher();
            fresher.setUser(user);
            fresher.setProgrammingLanguage(fresherReqDto.getProgrammingLanguage());
            fresher.setCenter(fresherReqDto.getCenter());
            return fresherRepository.save(fresher);
        }catch (RuntimeException e) {
            throw new AppException(ErrorCode.ENTER_MISS_INFO);
        }
    }
    
    // Lấy tất cả Freshers
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
        fresher.setUser(fresherDetails.getUser());
        fresher.setProgrammingLanguage(fresherDetails.getProgrammingLanguage());
        return fresherRepository.save(fresher);
    }

    // Xóa Fresher theo ID
    public Fresher deleteFresher(int id) {
        Fresher fresher = fresherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        fresher.setActive(false);
        return fresherRepository.save(fresher);
    }
}
