package com.carigo.notification.dto;

import com.carigo.notification.helper.ChannelType;
import com.carigo.notification.helper.NotificationEventType;

import lombok.Data;

@Data
public class NotificationTemplateRequest {

    private NotificationEventType eventType;
    private ChannelType channel;

    private String title;
    private String body;

    private Boolean active = true;
}
