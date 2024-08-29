package com.example.demo.service;

import com.example.demo.dto.request.CenterReqDto;
import com.example.demo.entity.Center;
import com.example.demo.repository.CenterRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.demo.dto.response.CenterResDto;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;

@Service
public class CenterService {

    @Autowired
    private CenterRepository centerRepository;

    @Autowired
    private UsersService usersService;
    // Thêm mới Center
    public Center addCenter(CenterReqDto centerReqDto) {
        Center center = new Center();
        center.setName(centerReqDto.getName());
        center.setLocation(centerReqDto.getLocation());
        try{
            center.setManager(usersService.getUser(centerReqDto.getManagerId()));
        }catch (AppException e){}
        return centerRepository.save(center);
    }

    public Center addCenter(String name, String location) {
        Center center = new Center();
        center.setName(name);
        center.setLocation(location);
        return centerRepository.save(center);
    }

    // Lấy tất cả Centers
    public List<CenterResDto> getAllCenters() { //
        return centerRepository.findAll().stream()
                .filter(Center::isActive)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Lấy Center theo ID
    public Optional<CenterResDto> getCenterById(int id) {
        try {
            return Optional.of(convertToDTO(getCenter(id)));
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }

    protected Center getCenter(int id) {
        return centerRepository.findById(id)
               .filter(Center::isActive)
               .orElseThrow(() -> new AppException(ErrorCode.CENTER_NOT_EXISTED));
    }
    // Cập nhật Center
    public CenterResDto updateCenter(int id, Center req) { // xxxxxxx
        Center center = getCenter(id);
        center.setName(req.getName());
        center.setLocation(req.getLocation());
        try{
            center.setManager(usersService.getUser(centerReqDto.getManagerId()));
        }catch (AppException e){}
        return convertToDTO(centerRepository.save(center));
    }

    // Xóa Center theo ID
    public Center deleteCenter(int id) {
        Center center = getCenter(id);
        center.setActive(false);
        return centerRepository.save(center);
    }

    protected List<Center> findCenters(int managerId) {
        return centerRepository.findByManagerId(managerId);
    }
    
    protected CenterResDto convertToDTO(Center center) {
        CenterResDto res = new CenterResDto();
        res.setName(center.getName());
        res.setLocation(center.getLocation());
        res.setIdManager(center.getManager().getId());
        res.setManager(center.getManager().getName());
        return res;
    }
}
