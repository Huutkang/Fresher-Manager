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

    protected List<Fresher> findFreshers(int centerId) {
        if (centerId > 0) {
            return fresherRepository.findByCenterId(centerId);
        } else {
            throw new IllegalArgumentException("At least one of userId or projectId must be provided");
        }
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
