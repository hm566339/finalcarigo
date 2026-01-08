package com.carigo.booking.dto;

import com.carigo.booking.helper.ChannelType;
import com.carigo.booking.helper.NotificationEventType;

import lombok.Data;

@Data
public class NotificationRequest {
    private Long userId;
    private NotificationEventType eventType;
    private ChannelType channel;

    private String email;
    private String phone;
    private String fcmToken;

    private String title;
    private String message;

    private String otp;
}
