package com.example.demo.service;

import com.example.demo.entity.Notification;
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
import com.example.demo.exception.ErrorCode;


@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private FresherService fresherService;

    @Autowired
    private ProjectService projectService;


    // Thêm mới Notification
    Notification addNotification(int fresher_id, int project_id, String message) {
        Notification notification = new Notification();
        notification.setSentAt(LocalDateTime.now());
        notification.setUser(fresherService.getFresher(fresher_id).getUser());
        notification.setProject(projectService.getProject(project_id));
        notification.setMessage(message);
        return notificationRepository.save(notification);
    }

    public Notification addNotification(NotificationReqDto req) {
        Notification notification = new Notification();
        notification.setSentAt(LocalDateTime.now());
        notification.setUser(fresherService.getFresher(req.getIdFresher()).getUser());
        notification.setProject(projectService.getProject(req.getIdProject()));
        notification.setMessage(req.getMessage());
        return notificationRepository.save(notification);
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
        .orElseThrow(() -> new AppException(ErrorCode.NOTIFICATION_NOT_EXISTED));
    }

    public Optional<NotificationResDto> getNotificationById(int id) {
        try {
            return Optional.of(convertToDTO(getNotification(id)));
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }

    // Cập nhật Notification
    public NotificationResDto updateNotification(int id, Notification notificationDetails) {
        Notification notification = getNotification(id);
        notification.setUser(fresherService.getFresher(req.getIdFresher()).getUser());
        notification.setProject(notificationDetails.getProject());
        notification.setMessage(notificationDetails.getMessage());
        notification.setSentAt(notificationDetails.getSentAt());
        return convertToDTO(notificationRepository.save(notification));
    }

    // Xóa Notification theo ID
    public void deleteNotification(int id) {
        notificationRepository.deleteById(id);
    }

    public List<Notification> findNotifications(int userId, int projectId) {
        if (userId > 0 && projectId > 0) {
            return notificationRepository.findByUserIdAndProjectId(userId, projectId);
        } else if (userId > 0) {
            return notificationRepository.findByUserId(userId);
        } else if (projectId > 0) {
            return notificationRepository.findByProjectId(projectId);
        } else {
            throw new IllegalArgumentException("Phải cung cấp ít nhất một trong số userId hoặc projectId");
        }
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
