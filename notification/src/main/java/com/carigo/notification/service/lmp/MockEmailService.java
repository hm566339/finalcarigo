package com.carigo.notification.service.lmp;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.carigo.notification.service.EmailService;

@Service
@Profile("dev")
public class MockEmailService extends EmailService {

    public MockEmailService() {
        super(null);
    }

    @Override
    public void sendEmail(String to, String subject, String message) {
        System.out.println("🧪 [MOCK EMAIL] To: " + to + " | " + subject);
    }
}
