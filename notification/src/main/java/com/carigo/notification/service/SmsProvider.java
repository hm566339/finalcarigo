package com.carigo.notification.service;

public interface SmsProvider {
    void sendSms(String phone, String message);
}
