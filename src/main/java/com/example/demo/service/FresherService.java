package com.example.demo.service;

import com.example.demo.entity.Fresher;
import com.example.demo.repository.FresherRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.demo.dto.internal.FresherDto;
import com.example.demo.dto.request.FresherReqDto;
import com.example.demo.dto.request.UpdateFresherReqDto;
import com.example.demo.dto.response.FresherResDto;
import com.example.demo.entity.Center;
import com.example.demo.entity.Users;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;


@Service
public class FresherService {

    @Autowired
    private FresherRepository fresherRepository;

    @Autowired
    private UsersService usersService;

    @Autowired
    private CenterService centerService;

    
    Fresher addFresher(Users user) {
        try{
            Fresher fresher = new Fresher();
            fresher.setUser(user);
            return fresherRepository.save(fresher);
        }catch (RuntimeException e) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    public FresherResDto addFresher(FresherDto fresherDto) {
        Users user = usersService.getUser(fresherDto.getIdUser());
        Fresher fresher = new Fresher();
        try{
            fresher.setUser(user);
            fresher.setProgrammingLanguage(fresherDto.getProgrammingLanguage());
            try{
                Center center = centerService.getCenter(fresherDto.getIdCenter());
                fresher.setCenter(center);
            } catch (AppException e) {}
            return convertToDTO(fresherRepository.save(fresher));
        }catch (RuntimeException e) {
            throw new AppException(ErrorCode.ENTER_MISS_INFO);
        }
    }

    public FresherResDto addFresher(FresherReqDto fresherReqDto) {
        try{
            Users user = usersService.addUser(fresherReqDto.getUsername(), fresherReqDto.getPassword(), fresherReqDto.getName(), fresherReqDto.getEmail(), fresherReqDto.getPhoneNumber());
            Fresher fresher = new Fresher();
            fresher.setUser(user);
            fresher.setProgrammingLanguage(fresherReqDto.getProgrammingLanguage());
            return convertToDTO(fresherRepository.save(fresher));
        }catch (RuntimeException e) {
            throw new AppException(ErrorCode.ENTER_MISS_INFO);
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
               .orElseThrow(() -> new AppException(ErrorCode.FRESHER_NOT_EXISTED));
        if (!fresher.getUser().isActive()) {
            fresher.setActive(false);
            fresherRepository.save(fresher);
            throw new AppException(ErrorCode.FRESHER_NOT_EXISTED);
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
        return convertToDTO(fresherRepository.save(fresher));
    }

    // Xóa Fresher theo ID
    public void deleteFresher(int id) {
        Fresher fresher = getFresher(id);
        fresher.setActive(false);
        fresherRepository.save(fresher);
    }

    protected Fresher getFresherByUserId(int userId) {
        Users user = usersService.getUser(userId);
        Fresher fresher = fresherRepository.findByUser(user)
                .filter(Fresher::isActive)
                .orElseThrow(() -> new AppException(ErrorCode.FRESHER_NOT_EXISTED));
        return fresher;
    }

    protected Fresher getFresherByCenterId(int centerId) {
        return fresherRepository.findByCenterId(centerId)
               .filter(Fresher::isActive)
               .orElseThrow(() -> new AppException(ErrorCode.FRESHER_NOT_EXISTED));
    }

    protected List<Fresher> getFresherByPhoneNumber(String phoneNumber){
        return fresherRepository.findByUser_PhoneNumber(phoneNumber).stream()
                .filter(Fresher::isActive)
                .collect(Collectors.toList());
    }

    protected Fresher getFresherByEmail(String email){
        return fresherRepository.findByUser_Email(email)
        .filter(Fresher::isActive)
        .orElseThrow(() -> new AppException(ErrorCode.FRESHER_NOT_EXISTED));
    }

    protected List<Fresher> findFreshersByName(String name) {
        return fresherRepository.findByUser_NameContainingIgnoreCase(name).stream()
                .filter(Fresher::isActive)
                .collect(Collectors.toList());
    }

    // Tìm fresher theo ngôn ngữ lập trình
    protected List<Fresher> findFreshersByProgrammingLanguage(String programmingLanguage) {
        return fresherRepository.findByProgrammingLanguageContainingIgnoreCase(programmingLanguage).stream()
                .filter(Fresher::isActive)
                .collect(Collectors.toList());
    }

    // Tìm fresher theo email
    protected List<Fresher> findFreshersByEmail(String email) {
        return fresherRepository.findByUser_EmailContainingIgnoreCase(email).stream()
                .filter(Fresher::isActive)
                .collect(Collectors.toList());
    }

    protected List<Fresher> findFreshersByPhoneNumber(String phoneNumber) {
        return fresherRepository.findByUser_PhoneNumberContainingIgnoreCase(phoneNumber).stream()
                .filter(Fresher::isActive)
                .collect(Collectors.toList());
    }

    protected List<Fresher> findFreshersByCenterName(String name) {
        return fresherRepository.findByCenterNameContainingIgnoreCase(name).stream()
                .filter(Fresher::isActive)
                .collect(Collectors.toList());
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
}
