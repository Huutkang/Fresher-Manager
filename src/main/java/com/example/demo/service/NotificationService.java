package com.example.demo.service;

import com.example.demo.entity.Notification;
import com.example.demo.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    // Thêm mới Notification
    public Notification addNotification(Notification notification) {
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
