package com.carigo.notification.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carigo.notification.helper.ChannelType;
import com.carigo.notification.helper.NotificationEventType;
import com.carigo.notification.model.NotificationTemplate;

public interface NotificationTemplateRepository
        extends JpaRepository<NotificationTemplate, Long> {

    boolean existsByEventTypeAndChannel(
            NotificationEventType eventType,
            ChannelType channel);

    Optional<NotificationTemplate> findByEventTypeAndChannelAndActiveTrue(
            NotificationEventType eventType,
            ChannelType channel);
}
