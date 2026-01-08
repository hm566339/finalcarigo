package com.carigo.notification.model;

import java.time.LocalDateTime;

import com.carigo.notification.helper.ChannelType;
import com.carigo.notification.helper.NotificationEventType;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "notification_logs")
public class NotificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private NotificationEventType eventType;

    @Enumerated(EnumType.STRING)
    private ChannelType channelType;

    private String target; // email / phone / fcm token
    private String status; // SUCCESS / FAILED

    private String errorMessage;

    private LocalDateTime createdAt = LocalDateTime.now();
}
