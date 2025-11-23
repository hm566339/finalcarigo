package com.hms.profile.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KycRequestConsumer {

    @Autowired
    private AadhaarKycService service;

    @KafkaListener(topics = "KYC_REQUEST", groupId = "kyc-worker")
    public void consume(Map<String, Object> payload) {

        Long jobId = Long.valueOf(payload.get("jobId").toString());
        Long profileId = Long.valueOf(payload.get("profileId").toString());
        String type = payload.get("type").toString();

        log.info("Processing KYC job {} for {}", jobId, type);

        service.processJob(jobId, profileId, type);
    }
}
