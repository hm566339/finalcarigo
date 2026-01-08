package com.carigo.notification.service;

import java.util.List;

import com.carigo.notification.dto.NotificationTemplateRequest;
import com.carigo.notification.model.NotificationTemplate;

public interface NotificationTemplateService {

    NotificationTemplate create(NotificationTemplateRequest request);

    NotificationTemplate update(Long id, NotificationTemplateRequest request);

    List<NotificationTemplate> list(int page, int size);

    void deactivate(Long id);
}
