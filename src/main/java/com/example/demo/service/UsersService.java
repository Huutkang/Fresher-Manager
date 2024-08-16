package com.example.demo.service;

import com.example.demo.entity.Users;
import com.example.demo.repository.UsersRepository;
import com.example.demo.dto.request.NewUserReqDto;
import com.example.demo.dto.request.SetUserReqDto;
import com.example.demo.dto.response.UserResDto;
import com.example.demo.exception.UserNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;




@Service
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;
    
    private final PasswordEncoder encoder = new BCryptPasswordEncoder(10);

    // Thêm mới User
    public Users addUser(NewUserReqDto user) {
        Users newUser = new Users();
        newUser.setUsername(user.getUsername());
        newUser.setPassword_hash(passwordEncoder(user.getPassword()));
        newUser.setName(user.getName());
        newUser.setEmail(user.getEmail());
        newUser.setPhoneNumber(user.getPhoneNumber());
        return usersRepository.save(newUser);
    }

    // Lấy tất cả Users dưới dạng userResDto
    public List<UserResDto> getAllUsers() {
        return usersRepository.findAll().stream()
                .filter(Users::getActive)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Hoặc sử dụng phân trang
    public Page<UserResDto> getAllUsers(Pageable pageable) {
        return usersRepository.findAll(pageable)
                .filter(Users::getActive)
                .map(this::convertToDTO);
    }

    // Lấy User theo ID dưới dạng userResDto
    public Optional<UserResDto> getUserById(int id) {
        try {
            return Optional.of(convertToDTO(getActiveUserById(id)));
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }

    // public Optional<UserResDto> getUserByUsername(String username) {
    //     try{
    //         int id = getUserIdByUsername(username).get();
    //         return getUserById(id);
    //     } catch (RuntimeException e) {
    //         return Optional.empty();
    //     }
    // }
    
    // chuyển username sang id
    protected Optional<Integer> getUserIdByUsername(String username) {
        return usersRepository.findByUsername(username)
                .filter(Users::getActive)
                .map(Users::getId);
    }
    
    // Phương thức hỗ trợ chuyển đổi từ Users sang userResDto
    private UserResDto convertToDTO(Users user) {
        UserResDto dto = new UserResDto();
        dto.setUsername(user.getUsername());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setRole(user.getRole());
        return dto;
    }

    protected Users setRole(int id, String role) {
        Users user = getActiveUserById(id);
        user.setRole(role);
        return usersRepository.save(user);
    }

    

    protected Users updatePassword(int id, String password) {
        Users user = getActiveUserById(id);
        user.setPassword_hash(passwordEncoder(password));
        return usersRepository.save(user);
    }
    
    protected Users updateUser(int id, SetUserReqDto userDto) {
        Users user = getActiveUserById(id);
        user.setUsername(userDto.getUsername());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());
        return usersRepository.save(user);
    }

    // Xóa User theo ID
    protected Users deleteUser(int id) {
        Users user = getActiveUserById(id);
        user.setActive(false);
        return usersRepository.save(user);
    }

    private String passwordEncoder(String password){
        return encoder.encode(password);
    }

    protected boolean checkPassword(int id, String password) {
        Users user = getActiveUserById(id);
        return encoder.matches(password, user.getPassword_hash());
    }

    protected Users getActiveUserById(int id) {
        return usersRepository.findById(id)
                .filter(Users::getActive)
                .orElseThrow(() -> new UserNotFoundException("User not found or inactive"));
    }
}

