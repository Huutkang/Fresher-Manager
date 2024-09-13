package com.example.demo.service;

import com.example.demo.entity.Notification;
import com.example.demo.enums.Code;
import com.example.demo.repository.NotificationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.demo.dto.request.NotificationReqDto;
import com.example.demo.dto.response.NotificationResDto;
import com.example.demo.exception.AppException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private FresherService fresherService;

    @Autowired
    private UsersService userService;

    @Autowired
    private ProjectService projectService;

    private static final Logger log = LogManager.getLogger(NotificationService.class);
    
    // Thêm mới Notification
    Notification addNotification(int idUser, int project_id, String message) {
        Notification notification = new Notification();
        notification.setSentAt(LocalDateTime.now());
        notification.setUser(userService.getUser(idUser));
        notification.setProject(projectService.getProject(project_id));
        notification.setMessage(message);
        log.info("Added notification ", idUser, project_id);
        return notificationRepository.save(notification);
    }

    public NotificationResDto addNotification(NotificationReqDto req) {
        Notification notification = new Notification();
        notification.setSentAt(LocalDateTime.now());
        notification.setUser(fresherService.getFresher(req.getIdFresher()).getUser());
        notification.setProject(projectService.getProject(req.getIdProject()));
        notification.setMessage(req.getMessage());
        log.info("Added notification ");
        return convertToDTO(notificationRepository.save(notification));
    }

    // Lấy tất cả Notifications
    public List<NotificationResDto> getAllNotifications() {
        return notificationRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Lấy Notification theo ID
    protected Notification getNotification(int id) {
        return notificationRepository.findById(id)
        .orElseThrow(() -> new AppException(Code.NOTIFICATION_NOT_EXISTED));
    }

    public Optional<NotificationResDto> getNotificationById(int id) {
        try {
            return Optional.of(convertToDTO(getNotification(id)));
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }

    // Cập nhật Notification
    public NotificationResDto updateNotification(int id, NotificationReqDto notificationDto) {
        Notification notification = getNotification(id);
        if (notificationDto.getIdFresher() != null) {
            notification.setUser(fresherService.getFresher(notificationDto.getIdFresher()).getUser());
        }
        if (notificationDto.getIdProject() != null) {
            notification.setProject(projectService.getProject(notificationDto.getIdProject()));
        }
        if (notificationDto.getMessage() != null) {
            notification.setMessage(notificationDto.getMessage());
        }
        notification.setSentAt(LocalDateTime.now());
        log.info("Updated notification");
        return convertToDTO(notificationRepository.save(notification));
    }
    
    

    // Xóa Notification theo ID
    public void deleteNotification(int id) {
        notificationRepository.deleteById(id);
        log.info("Deleted notification");
    }

    public List<NotificationResDto> findNotifications(int userId, int projectId) {
        List<Notification> notifications;
        if (userId > 0 && projectId > 0) {
            notifications = notificationRepository.findByUserIdAndProjectId(userId, projectId);
        } else if (userId > 0) {
            notifications = notificationRepository.findByUserId(userId);
        } else if (projectId > 0) {
            notifications = notificationRepository.findByProjectId(projectId);
        } else {
            throw new IllegalArgumentException("Phải cung cấp ít nhất một trong số userId hoặc projectId");
        }
        return notifications.stream()
                            .map(this::convertToDTO)
                            .collect(Collectors.toList());
    }
    
    
    protected NotificationResDto convertToDTO(Notification notification) {
        NotificationResDto res = new NotificationResDto();
        res.setId(notification.getId());
        res.setIdUser(notification.getUser().getId());
        res.setIdProject(notification.getProject().getId());
        res.setUser(notification.getUser().getName());
        res.setProject(notification.getProject().getName());
        res.setMessage(notification.getMessage());
        return res;
    }
}
