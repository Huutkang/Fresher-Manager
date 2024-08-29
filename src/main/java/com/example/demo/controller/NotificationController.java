package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.request.NotificationReqDto;
import com.example.demo.dto.response.NotificationResDto;
import com.example.demo.entity.Notification;
import com.example.demo.service.NotificationService;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    // Thêm mới Notification
    @PostMapping
    public Notification createNotification(@RequestBody NotificationReqDto notification) {
        return notificationService.addNotification(notification);
    }

    // Lấy tất cả Notifications
    @GetMapping
    public List<NotificationResDto> getAllNotifications() {
        return notificationService.getAllNotifications();
    }

    // Lấy Notification theo ID
    @GetMapping("/{id}")
    public ResponseEntity<NotificationResDto> getNotificationById(@PathVariable int id) {
        NotificationResDto notification = notificationService.getNotificationById(id).orElse(null);
        if (notification == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(notification);
    }

    // Cập nhật Notification
    @PutMapping("/{id}")
    public ResponseEntity<NotificationResDto> updateNotification(@PathVariable int id, @RequestBody Notification notificationDetails) {
        try {
            NotificationResDto updatedNotification = notificationService.updateNotification(id, notificationDetails);
            return ResponseEntity.ok(updatedNotification);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Xóa Notification theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable int id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }
}
