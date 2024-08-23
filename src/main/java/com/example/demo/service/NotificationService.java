package com.example.demo.service;

import com.example.demo.entity.Notification;
import com.example.demo.repository.NotificationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    public Notification addNotification(int fresher_id, int project_id, String message) {
        Notification notification = new Notification();
        notification.setSentAt(LocalDateTime.now());
        notification.setFresher(fresherService.getFresherById(fresher_id).orElseThrow(() -> new AppException(ErrorCode.FRESHER_NOT_EXISTED)));
        notification.setProject(projectService.getProjectById(project_id).orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_EXISTED)));
        notification.setMessage(message);
        return notificationRepository.save(notification);
    }

    // Lấy tất cả Notifications
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    // Lấy Notification theo ID
    public Optional<Notification> getNotificationById(int id) {
        return notificationRepository.findById(id);
    }

    // Cập nhật Notification
    public Notification updateNotification(int id, Notification notificationDetails) {
        Notification notification = notificationRepository.findById(id).orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setFresher(notificationDetails.getFresher());
        notification.setProject(notificationDetails.getProject());
        notification.setMessage(notificationDetails.getMessage());
        notification.setSentAt(notificationDetails.getSentAt());
        return notificationRepository.save(notification);
    }

    // Xóa Notification theo ID
    public void deleteNotification(int id) {
        notificationRepository.deleteById(id);
    }
}
