package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByUserIdAndProjectId(int userId, int projectId);
    List<Notification> findByUserId(int userId);
    List<Notification> findByProjectId(int projectId);
}
