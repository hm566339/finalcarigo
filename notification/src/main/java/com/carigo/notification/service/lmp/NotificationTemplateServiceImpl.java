package com.carigo.notification.service.lmp;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.carigo.notification.dto.NotificationTemplateRequest;
import com.carigo.notification.model.NotificationTemplate;
import com.carigo.notification.repository.NotificationTemplateRepository;
import com.carigo.notification.service.NotificationTemplateService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationTemplateServiceImpl
        implements NotificationTemplateService {

    private final NotificationTemplateRepository repository;

    @Override
    public NotificationTemplate create(NotificationTemplateRequest req) {

        if (repository.existsByEventTypeAndChannel(
                req.getEventType(), req.getChannel())) {
            throw new RuntimeException("Template already exists");
        }

        NotificationTemplate t = new NotificationTemplate();
        t.setEventType(req.getEventType());
        t.setChannel(req.getChannel());
        t.setTitle(req.getTitle());
        t.setBody(req.getBody());
        t.setActive(req.getActive());

        return repository.save(t);
    }

    @Override
    public NotificationTemplate update(Long id,
            NotificationTemplateRequest req) {

        NotificationTemplate t = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Template not found"));

        t.setTitle(req.getTitle());
        t.setBody(req.getBody());
        t.setActive(req.getActive());

        return repository.save(t);
    }

    @Override
    public List<NotificationTemplate> list(int page, int size) {
        return repository
                .findAll(PageRequest.of(page, size))
                .getContent();
    }

    @Override
    public void deactivate(Long id) {
        NotificationTemplate t = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Template not found"));

        t.setActive(false);
        repository.save(t);
    }
}
