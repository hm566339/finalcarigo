package com.carigo.notification.service.lmp;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.carigo.notification.service.SmsProvider;

@Service
@ConditionalOnProperty(prefix = "notification.sms", name = "provider", havingValue = "MOCK")
public class MockSmsProvider implements SmsProvider {

    @Override
    public void sendSms(String phone, String message) {
        System.out.println("🧪 [MOCK SMS] To: " + phone + " | Msg: " + message);
    }
}
