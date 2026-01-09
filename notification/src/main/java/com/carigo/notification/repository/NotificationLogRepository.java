package com.carigo.notification.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carigo.notification.helper.NotificationStatus;
import com.carigo.notification.model.NotificationLog;

public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long> {
    List<NotificationLog> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<NotificationLog> findByStatus(String status);

    long countByStatus(String status);

    long countByUserIdAndStatus(Long userId, NotificationStatus unread);

}
