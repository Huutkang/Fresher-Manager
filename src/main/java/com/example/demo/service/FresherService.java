package com.example.demo.service;

import com.example.demo.entity.Fresher;
import com.example.demo.repository.FresherRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import com.example.demo.dto.internal.FresherDto;
import com.example.demo.dto.request.FresherReqDto;
import com.example.demo.dto.request.UpdateFresherReqDto;
import com.example.demo.dto.response.FresherResDto;
import com.example.demo.entity.Center;
import com.example.demo.entity.Users;
import com.example.demo.enums.Code;
import com.example.demo.exception.AppException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.example.demo.enums.Role;


@Service
public class FresherService {

    @Autowired
    private FresherRepository fresherRepository;

    @Autowired
    private UsersService usersService;

    @Autowired
    private CenterService centerService;

    private static final Logger log = LogManager.getLogger(FresherService.class);
    
    Fresher addFresher(Users user) {
        try{
            Fresher fresher = new Fresher();
            usersService.addRole(user.getId(), Role.FRESHER);
            fresher.setUser(user);
            log.info("Fresher added");
            return fresherRepository.save(fresher);
        }catch (RuntimeException e) {
            throw new AppException(Code.UNCATEGORIZED_EXCEPTION);
        }
    }

    public FresherResDto addFresher(FresherDto fresherDto) {
        int idUser = fresherDto.getIdUser();
        Users user = usersService.getUser(idUser);
        Fresher fresher = new Fresher();
        try{
            fresher.setUser(user);
            fresher.setProgrammingLanguage(fresherDto.getProgrammingLanguage());
            try{
                Center center = centerService.getCenter(fresherDto.getIdCenter());
                fresher.setCenter(center);
            } catch (AppException e) {}
            usersService.addRole(idUser, Role.FRESHER);
            return convertToDTO(fresherRepository.save(fresher));
        }catch (RuntimeException e) {
            log.error("Fresher not added\n"+e);
            throw new AppException(Code.ENTER_MISS_INFO);
        }
    }

    public FresherResDto addFresher(FresherReqDto fresherReqDto) {
        try{
            Users user = usersService.addUser(generateRandomString(), fresherReqDto.getPassword(), fresherReqDto.getName(), fresherReqDto.getEmail(), fresherReqDto.getPhoneNumber());
            Fresher fresher = new Fresher();
            fresher.setUser(user);
            fresher.setProgrammingLanguage(fresherReqDto.getProgrammingLanguage());
            usersService.addRole(user.getId(), Role.FRESHER);
            return convertToDTO(fresherRepository.save(fresher));
        }catch (RuntimeException e) {
            log.error("Fresher not added\n"+e);
            throw new AppException(Code.ENTER_MISS_INFO);
        }
    }
    
    // Lấy tất cả Freshers
    public List<FresherResDto> getAllFreshers() {
        return fresherRepository.findAll().stream()
                .filter(fresher -> fresher.isActive() && fresher.getUser().isActive())
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    

    protected Fresher getFresher(int id) {
        Fresher fresher = fresherRepository.findById(id)
               .filter(Fresher::isActive)
               .orElseThrow(() -> new AppException(Code.FRESHER_NOT_EXISTED));
        if (!fresher.getUser().isActive()) {
            fresher.setActive(false);
            fresherRepository.save(fresher);
            throw new AppException(Code.FRESHER_NOT_EXISTED);
        }
        return fresher;
    }

    // Lấy Fresher theo ID
    public Optional<FresherResDto> getFresherById(int id) {
        try {
            return Optional.of(convertToDTO(getFresher(id)));
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }

    // Cập nhật Fresher
    public FresherResDto updateFresher(int id, UpdateFresherReqDto req) {
        Fresher fresher = getFresher(id);
        fresher.setProgrammingLanguage(req.getProgrammingLanguage());
        Center center = centerService.getCenter(req.getIdCenter());
        fresher.setCenter(center);
        log.info("Updated fresher", id);
        return convertToDTO(fresherRepository.save(fresher));
    }

    // Xóa Fresher theo ID
    public void deleteFresher(int id) {
        Fresher fresher = getFresher(id);
        fresher.setActive(false);
        fresherRepository.save(fresher);
        log.info("Deleted fresher", id);
    }

    // Hàm lấy Fresher theo ID người dùng
    public FresherResDto getFresherByUserId(int userId) {
        Users user = usersService.getUser(userId);
        Fresher fresher = fresherRepository.findByUser(user)
                .filter(Fresher::isActive)
                .orElseThrow(() -> new AppException(Code.FRESHER_NOT_EXISTED));
        return convertToDTO(fresher);
    }

    // Lấy Fresher theo trung tâm
    public List<FresherResDto> getFresherByCenterId(int centerId) {
        return fresherRepository.findByCenterId(centerId).stream()
               .filter(Fresher::isActive)
               .map(this::convertToDTO)
               .collect(Collectors.toList());
    }

    // Lấy Fresher theo số điện thoại
    public List<FresherResDto> getFresherByPhoneNumber(String phoneNumber) {
        return fresherRepository.findByUser_PhoneNumber(phoneNumber).stream()
               .filter(Fresher::isActive)
               .map(this::convertToDTO)
               .collect(Collectors.toList());
    }

    // Lấy Fresher theo email
    public FresherResDto getFresherByEmail(String email) {
        Fresher fresher = fresherRepository.findByUser_Email(email)
        .filter(Fresher::isActive)
        .orElseThrow(() -> new AppException(Code.FRESHER_NOT_EXISTED));
        return convertToDTO(fresher);
    }

    // Tìm Fresher theo tên
    public List<FresherResDto> findFreshersByName(String name) {
        return fresherRepository.findByUser_NameContainingIgnoreCase(name).stream()
                .filter(Fresher::isActive)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Tìm Fresher theo ngôn ngữ lập trình
    public List<FresherResDto> findFreshersByProgrammingLanguage(String programmingLanguage) {
        return fresherRepository.findByProgrammingLanguageContainingIgnoreCase(programmingLanguage).stream()
                .filter(Fresher::isActive)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Tìm Fresher theo email
    public List<FresherResDto> findFreshersByEmail(String email) {
        return fresherRepository.findByUser_EmailContainingIgnoreCase(email).stream()
                .filter(Fresher::isActive)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Tìm Fresher theo số điện thoại
    public List<FresherResDto> findFreshersByPhoneNumber(String phoneNumber) {
        return fresherRepository.findByUser_PhoneNumberContainingIgnoreCase(phoneNumber).stream()
                .filter(Fresher::isActive)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Tìm Fresher theo tên trung tâm
    public List<FresherResDto> findFreshersByCenterName(String name) {
        return fresherRepository.findByCenterNameContainingIgnoreCase(name).stream()
                .filter(Fresher::isActive)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    
    public long countFresher() {
        return fresherRepository.count();
    }

    public FresherResDto convertToDTO(Fresher fresher){
        FresherResDto fresherResDto = new FresherResDto();
        Users user = fresher.getUser();
        fresherResDto.setId(fresher.getId());
        fresherResDto.setIdUser(user.getId());
        fresherResDto.setUsername(user.getUsername());
        fresherResDto.setName(user.getName());
        fresherResDto.setEmail(user.getEmail());
        fresherResDto.setPhoneNumber(user.getPhoneNumber());
        fresherResDto.setProgrammingLanguage(fresher.getProgrammingLanguage());
        fresherResDto.setCenter(fresher.getCenter());
        return fresherResDto;
    }


    private static String generateRandomString() {
        String lowercaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String allowedChars = lowercaseLetters + numbers;
        
        Random random = new Random();
        int length = random.nextInt(16) + 5; // Độ dài từ 5 đến 20
        
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(allowedChars.length());
            sb.append(allowedChars.charAt(randomIndex));
        }
        return sb.toString();
    }
}
