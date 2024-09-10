package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.request.NotificationReqDto;
import com.example.demo.dto.response.Api;
import com.example.demo.dto.response.NotificationResDto;
import com.example.demo.enums.Code;
import com.example.demo.service.NotificationService;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    // Thêm mới Notification
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PostMapping
    public ResponseEntity<Api<NotificationResDto>> createNotification(@RequestBody NotificationReqDto notification) {
        NotificationResDto createdNotification = notificationService.addNotification(notification);
        return Api.response(Code.OK, createdNotification);
    }

    // Lấy tất cả Notifications
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @GetMapping
    public ResponseEntity<Api<List<NotificationResDto>>> getAllNotifications() {
        List<NotificationResDto> notifications = notificationService.getAllNotifications();
        return Api.response(Code.OK, notifications);
    }

    // Lấy Notification theo ID
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Api<NotificationResDto>> getNotificationById(@PathVariable int id) {
        return notificationService.getNotificationById(id)
                .map(notification -> Api.response(Code.OK, notification))
                .orElseGet(() -> Api.response(Code.NOTIFICATION_NOT_EXISTED));
    }

    // Cập nhật Notification
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Api<NotificationResDto>> updateNotification(@PathVariable int id, @RequestBody NotificationReqDto notificationDetails) {
        try {
            NotificationResDto updatedNotification = notificationService.updateNotification(id, notificationDetails);
            return Api.response(Code.OK, updatedNotification);
        } catch (RuntimeException e) {
            return Api.response(Code.NOTIFICATION_NOT_EXISTED);
        }
    }

    // Xóa Notification theo ID
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Api<Void>> deleteNotification(@PathVariable int id) {
        notificationService.deleteNotification(id);
        return Api.response(Code.OK);
    }
}
