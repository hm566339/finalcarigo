package com.carigo.notification.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.carigo.notification.dto.NotificationRequest;
import com.carigo.notification.helper.ChannelType;
import com.carigo.notification.helper.NotificationEventType;
import com.carigo.notification.helper.NotificationStatus;
import com.carigo.notification.model.NotificationLog;
import com.carigo.notification.repository.NotificationLogRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final EmailService emailService;
    private final SmsProvider smsProvider;
    private final FcmPushService fcmPushService;
    private final NotificationLogRepository logRepository;

    public void send(NotificationRequest request) {

        // EMAIL
        if (request.getChannel() == ChannelType.EMAIL || request.getChannel() == ChannelType.ALL) {
            logAndSend(ChannelType.EMAIL, request.getEmail(), request,
                    () -> emailService.sendEmail(
                            request.getEmail(),
                            request.getTitle(),
                            request.getMessage()));
        }

        // SMS
        if (request.getChannel() == ChannelType.SMS || request.getChannel() == ChannelType.ALL
                || request.getEventType() == NotificationEventType.OTP_SMS) {

            String msg = request.getEventType() == NotificationEventType.OTP_SMS
                    ? "Your OTP for Carigo is " + request.getOtp()
                    : request.getMessage();

            logAndSend(ChannelType.SMS, request.getPhone(), request,
                    () -> smsProvider.sendSms(request.getPhone(), msg));
        }

        // PUSH
        if (request.getChannel() == ChannelType.PUSH || request.getChannel() == ChannelType.ALL) {
            logAndSend(ChannelType.PUSH, request.getFcmToken(), request,
                    () -> fcmPushService.sendPushToToken(
                            request.getFcmToken(),
                            request.getTitle(),
                            request.getMessage()));
        }
    }

    private void logAndSend(ChannelType channel,
            String target,
            NotificationRequest req,
            Runnable action) {

        NotificationLog log = new NotificationLog();
        log.setUserId(req.getUserId());
        log.setEventType(req.getEventType());
        log.setChannelType(channel);
        log.setTarget(target);

        try {
            action.run();
            log.setStatus(NotificationStatus.UNREAD); // ✅ SUCCESS → UNREAD
        } catch (Exception e) {
            log.setStatus(NotificationStatus.FAILED); // ✅ enum
            log.setErrorMessage(e.getMessage());
        }

        logRepository.save(log);
    }

    public List<NotificationLog> getByUser(Long userId) {
        return logRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public List<NotificationLog> status() {
        return logRepository.findByStatus(null);
    }

    public List<NotificationLog> getFailedNotifications() {

        return logRepository.findByStatus("FAILED");
    }

    public List<NotificationLog> getAllNotifications(int page, int size) {

        return logRepository
                .findAll(PageRequest.of(page, size))
                .getContent();
    }

    public void retry(Long logId) {

        NotificationLog log = logRepository.findById(logId)
                .orElseThrow(() -> new RuntimeException("Log not found"));

        NotificationRequest req = new NotificationRequest();
        req.setUserId(log.getUserId());
        req.setEventType(log.getEventType());
        req.setChannel(log.getChannelType());
        req.setTitle("Retry Notification");
        req.setMessage("Retrying failed notification");

        if (log.getChannelType() == ChannelType.EMAIL)
            req.setEmail(log.getTarget());
        if (log.getChannelType() == ChannelType.SMS)
            req.setPhone(log.getTarget());

        send(req);
    }

    public void sendOtp(String phone, String otp) {

        NotificationRequest req = new NotificationRequest();
        req.setChannel(ChannelType.SMS);
        req.setEventType(NotificationEventType.OTP_SMS);
        req.setPhone(phone);
        req.setOtp(otp);
        req.setTitle("OTP Verification");
        req.setMessage("Your OTP for Carigo is " + otp);

        send(req); // 🔁 reuse existing send logic
    }

    public Map<String, Long> getNotificationStats() {

        long total = logRepository.count();
        long success = logRepository.countByStatus("SUCCESS");
        long failed = logRepository.countByStatus("FAILED");

        return Map.of(
                "TOTAL", total,
                "SUCCESS", success,
                "FAILED", failed);
    }

    public long unreadCount(Long userId) {
        return logRepository.countByUserIdAndStatus(
                userId,
                NotificationStatus.UNREAD);

    }
}
