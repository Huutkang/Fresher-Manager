package com.example.demo.service;

import com.example.demo.dto.request.CenterReqDto;
import com.example.demo.entity.Center;
import com.example.demo.enums.Code;
import com.example.demo.repository.CenterRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.demo.dto.response.CenterResDto;
import com.example.demo.exception.AppException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



@Service
public class CenterService {

    @Autowired
    private CenterRepository centerRepository;

    @Autowired
    private UsersService usersService;

    private static final Logger log = LogManager.getLogger(CenterService.class);

    // Thêm mới Center
    public CenterResDto addCenter(CenterReqDto centerReqDto) {
        Center center = new Center();
        center.setName(centerReqDto.getName());
        center.setLocation(centerReqDto.getLocation());
        try {
            center.setManager(usersService.getUser(centerReqDto.getIdManager()));
        } catch (AppException e) {
            log.error("Manager not found");
        }
        return convertToDTO(centerRepository.save(center));
    }

    public Center addCenter(String name, String location) {
        Center center = new Center();
        center.setName(name);
        center.setLocation(location);
        return centerRepository.save(center);
    }

    // Lấy tất cả Centers
    public List<CenterResDto> getAllCenters() { 
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
               .orElseThrow(() -> new AppException(Code.CENTER_NOT_EXISTED));
    }

    // Cập nhật Center
    public CenterResDto updateCenter(int id, CenterReqDto req) {
        Center center = getCenter(id);
        center.setName(req.getName());
        center.setLocation(req.getLocation());
        try {
            center.setManager(usersService.getUser(req.getManagerId()));
        } catch (AppException e) {
            log.error("Manager not found");
        }
        return convertToDTO(centerRepository.save(center));
    }

    // Xóa Center theo ID
    public Center deleteCenter(int id) {
        Center center = getCenter(id);
        center.setActive(false);
        log.info("Deleting Center " + id);
        return centerRepository.save(center);
    }

    // Tìm Center theo Manager ID và trả về DTO
    public Optional<CenterResDto> getCenterByManagerId(int managerId) {
        return centerRepository.findByManagerId(managerId)
               .filter(Center::isActive)
               .map(this::convertToDTO);
    }

    // Tìm kiếm Center theo tên và trả về DTO
    public List<CenterResDto> findByName(String name) {
        return centerRepository.findByNameContainingIgnoreCase(name)
               .stream()
               .filter(Center::isActive)
               .map(this::convertToDTO)
               .collect(Collectors.toList());
    }

    // Tìm kiếm Center theo vị trí và trả về DTO
    public List<CenterResDto> findByLocation(String location) {
        return centerRepository.findByLocationContainingIgnoreCase(location)
               .stream()
               .filter(Center::isActive)
               .map(this::convertToDTO)
               .collect(Collectors.toList());
    }

    public long countCenter() {
        return centerRepository.count();
    }

    // Chuyển đổi từ entity sang DTO
    protected CenterResDto convertToDTO(Center center) {
        CenterResDto res = new CenterResDto();
        res.setId(center.getId());
        res.setName(center.getName());
        res.setLocation(center.getLocation());
        res.setIdManager(center.getManager().getId());
        res.setManager(center.getManager().getName());
        return res;
    }
}
