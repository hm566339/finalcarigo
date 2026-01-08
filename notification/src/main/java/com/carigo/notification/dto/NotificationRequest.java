package com.carigo.notification.dto;

import com.carigo.notification.helper.ChannelType;
import com.carigo.notification.helper.NotificationEventType;

import lombok.Data;

@Data
public class NotificationRequest {

    private Long userId; // owner/renter id
    private NotificationEventType eventType;
    private ChannelType channel; // EMAIL/SMS/PUSH/ALL

    private String email; // optional
    private String phone; // optional
    private String fcmToken; // for push

    private String title; // notification title
    private String message; // notification body

    private String otp; // for OTP_SMS
}
