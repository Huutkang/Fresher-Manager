package com.example.demo.service;

import java.util.HashSet;

import com.example.demo.entity.Users;
import com.example.demo.repository.UsersRepository;
import com.example.demo.dto.request.NewUserReqDto;
import com.example.demo.dto.request.SetUserReqDto;
import com.example.demo.dto.response.UserResDto;
import com.example.demo.exception.ErrorCode;
import com.example.demo.exception.AppException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.demo.enums.Role;




@Service
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;
    
    private final PasswordEncoder encoder = new BCryptPasswordEncoder(10);

    // Thêm mới User
    public Users addUser(NewUserReqDto user) {
        try{
            Users newUser = new Users();
            HashSet<String> roles = new HashSet<>();
            newUser.setUsername(user.getUsername());
            newUser.setPassword_hash(passwordEncoder(user.getPassword()));
            newUser.setName(user.getName());
            newUser.setEmail(user.getEmail());
            newUser.setPhoneNumber(user.getPhoneNumber());
            roles.add(Role.USER.name());
            newUser.setRoles(roles);
            return usersRepository.save(newUser);
        }catch (RuntimeException e) {
            throw new AppException(ErrorCode.ENTER_MISS_INFO);
        }
    }

    public Users addUser(String userName, String password, String name, String email, String phoneNumber) {
        try{
            Users newUser = new Users();
            HashSet<String> roles = new HashSet<>();
            newUser.setUsername(userName);
            newUser.setPassword_hash(passwordEncoder(password));
            newUser.setName(name);
            newUser.setEmail(email);
            newUser.setPhoneNumber(phoneNumber);
            roles.add(Role.USER.name());
            newUser.setRoles(roles);
            return usersRepository.save(newUser);
        }catch (RuntimeException e) {
            throw new AppException(ErrorCode.ENTER_MISS_INFO);
        }
    }

    // hàm này tạo duy nhất một admin id=1, còn lại add role admin cho user
    public void newAdmin(String name, String password){
        try{
            Users admin = new Users();
            admin.setUsername("admin");
            admin.setPassword_hash(passwordEncoder(password));
            admin.setName(name);
            Set<String> roles = new HashSet<>();
            roles.add(Role.ADMIN.name());
            roles.add(Role.USER.name());
            admin.setRoles(roles);
            usersRepository.save(admin);
        }catch(RuntimeException e){
            throw new AppException(ErrorCode.USER_EXISTED);
        }
    }

    // Lấy tất cả Users dưới dạng userResDto
    public List<UserResDto> getAllUsers() {
        return usersRepository.findAll().stream()
                .filter(Users::isActive)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Hoặc sử dụng phân trang
    public Page<UserResDto> getAllUsers(Pageable pageable) {
        Page<Users> usersPage = usersRepository.findAll(pageable);
        List<UserResDto> filteredAndMappedUsers = usersPage.stream()
                .filter(Users::isActive)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(filteredAndMappedUsers, pageable, usersPage.getTotalElements());
    }

    // Lấy User theo ID dưới dạng userResDto
    public Optional<UserResDto> getUserById(int id) {
        try {
            return Optional.of(convertToDTO(getUser(id)));
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }

    public Optional<UserResDto> getUserByUsername(String username) {
        try{
            return Optional.of(convertToDTO(getUser(username)));
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }
    
    protected Users getUser(int id) {
        return usersRepository.findById(id)
                .filter(Users::isActive)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    protected Users getUser(String username) {
        return usersRepository.findByUsername(username)
                .filter(Users::isActive)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    // chuyển username sang id
    protected int getUserIdByUsername(String username) {
        return usersRepository.findByUsername(username)
                .filter(Users::isActive)
                .map(Users::getId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }
    
    // Phương thức hỗ trợ chuyển đổi từ Users sang userResDto
    public UserResDto convertToDTO(Users user) {
        UserResDto dto = new UserResDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setRoles(user.getRoles());
        return dto;
    }

    protected void addRole(int id, Role role) {
        if (id<2) return;
        Users user = getUser(id);
        Set<String> roles = user.ss();
        roles.add(role.name());
        user.setRoles(roles);
        usersRepository.save(user);
    }

    protected void removeRole(int id, Role role) {
        if (id<2) return;
        Users user = getUser(id);
        Set<String> roles = user.ss();
        roles.remove(role.name());
        user.setRoles(roles);
        usersRepository.save(user);
    }

    protected Users updatePassword(int id, String password) {
        Users user = getUser(id);
        user.setPassword_hash(passwordEncoder(password));
        return usersRepository.save(user);
    }
    
    public Users updateUser(int id, SetUserReqDto userDto) {
        Users user = getUser(id);
        try{
            user.setUsername(userDto.getUsername());
            user.setName(userDto.getName());
            user.setEmail(userDto.getEmail());
            user.setPhoneNumber(userDto.getPhoneNumber());
            return usersRepository.save(user);
        }catch (RuntimeException e) {
            throw new AppException(ErrorCode.ENTER_MISS_INFO);
        }
    }

    // Xóa User theo ID
    public void deleteUser(int id) {
        if (id<2) return;
        Users user = getUser(id);
        user.setActive(false);
        usersRepository.save(user);
    }

    private String passwordEncoder(String password){
        return encoder.encode(password);
    }

    protected boolean checkPassword(int id, String password) {
        try{
            Users user = getUser(id);
            return encoder.matches(password, user.getPassword_hash());
        } catch (RuntimeException e) {
            return false;
        }
    }

    protected boolean checkPassword(String username, String password) {
        try{
            Users user = getUser(username);
            return encoder.matches(password, user.getPassword_hash());
        } catch (RuntimeException e) {
            return false;
        }
    }
    
}

