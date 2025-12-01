package com.hms.profile.service;

import com.hms.profile.helper.KycStatus;
import com.hms.profile.model.*;
import com.hms.profile.repository.*;
import com.hms.profile.external.ExternalAadhaarClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AadhaarKycService {

    private final ExternalAadhaarClient aadhaarClient;
    private final ProfileKycHistoryRepository historyRepo;
    private final KycJobRepository jobRepo;
    private final KycConsentRepository consentRepo;
    private final CarOwnerRepository ownerRepo;
    private final RenterRepository renterRepo;
    private final KafkaTemplate<String, Object> kafka;

    // PUBLIC entrypoint: create job and return job id
    public Long enqueueVerificationForOwner(Long ownerId) {
        checkConsent(ownerId, "OWNER");

        KycJob job = new KycJob();
        job.setProfileId(ownerId);
        job.setProfileType("OWNER");
        job.setStatus("QUEUED");
        jobRepo.save(job);

        // ---- SEND TO KAFKA ----
        kafka.send("KYC_REQUEST",
                Map.of(
                        "jobId", job.getId(),
                        "profileId", ownerId,
                        "type", "OWNER"));

        return job.getId();
    }

    @Transactional
    public Long enqueueVerificationForRenter(Long renterId) {
        checkConsent(renterId, "RENTER");

        KycJob job = new KycJob();
        job.setProfileId(renterId);
        job.setProfileType("RENTER");
        job.setStatus("QUEUED");
        jobRepo.save(job);

        processRenterKycAsync(job.getId(), renterId);
        return job.getId();
    }

    private void checkConsent(Long profileId, String type) {
        Optional<KycConsent> consent = consentRepo.findTopByProfileIdOrderByConsentedAtDesc(profileId);
        if (consent.isEmpty() || !Boolean.TRUE.equals(consent.get().getConsentGiven())) {
            throw new IllegalStateException("User consent not found for KYC");
        }
    }

    // ASYNC worker methods (non-blocking caller returns fast)
    @Async("kycExecutor")
    public void processOwnerKycAsync(Long jobId, Long ownerId) {
        processJob(jobId, ownerId, "OWNER");
    }

    @Async("kycExecutor")
    public void processRenterKycAsync(Long jobId, Long renterId) {
        processJob(jobId, renterId, "RENTER");
    }

    @Transactional
    protected void processJob(Long jobId, Long profileId, String profileType) {
        KycJob job = jobRepo.findById(jobId).orElseThrow();
        job.setStatus("RUNNING");
        jobRepo.save(job);

        try {
            if ("OWNER".equals(profileType)) {
                CarOwner owner = ownerRepo.findById(profileId).orElseThrow();
                boolean ok = verifyAgainstAadhaar(owner.getAadhaarNumber(), owner.getName(), owner.getDob());
                owner.setKycStatus(ok ? KycStatus.VERIFIED.name() : KycStatus.FAILED.name());
                ownerRepo.save(owner);
                saveHistory(profileId, profileType, ok ? KycStatus.VERIFIED : KycStatus.FAILED,
                        ok ? "Aadhaar matched" : "Aadhaar mismatch");
            } else {
                Renter renter = renterRepo.findById(profileId).orElseThrow();
                boolean ok = verifyAgainstAadhaar(renter.getAadhaarNumber(), renter.getName(), renter.getDob());
                renter.setKycStatus(ok ? KycStatus.VERIFIED.name() : KycStatus.FAILED.name());
                renterRepo.save(renter);
                saveHistory(profileId, profileType, ok ? KycStatus.VERIFIED : KycStatus.FAILED,
                        ok ? "Aadhaar matched" : "Aadhaar mismatch");
            }

            job.setStatus("SUCCESS");
            job.setDetail("Completed");
            jobRepo.save(job);

        } catch (Exception ex) {
            log.error("KYC job failed: {}", ex.getMessage(), ex);
            job.setStatus("FAILED");
            job.setDetail(ex.getMessage());
            jobRepo.save(job);
            // mark profile as MANUAL_CHECK
            saveHistory(profileId, profileType, KycStatus.MANUAL_CHECK, "KYC processing error");
        }
    }

    protected boolean verifyAgainstAadhaar(String aadhaarNumber, String localName, LocalDate localDob) {
        // NOTE: DO NOT pass plain aadhaar in logs. Caller should hash/encrypt before
        // passing if needed.
        Map<String, Object> external = aadhaarClient.verifyAadhaar(aadhaarNumber);
        String status = (String) external.get("status"); // VALID / INVALID / UNAVAILABLE
        String extName = (String) external.get("name");
        String extDob = (String) external.get("dob"); // ISO yyyy-MM-dd or vendor format

        boolean statusOk = "VALID".equalsIgnoreCase(status);
        boolean nameOk = extName != null && extName.equalsIgnoreCase(localName);
        boolean dobOk = (extDob != null && extDob.equals(localDob.toString()));

        return statusOk && nameOk && dobOk;
    }

    protected void saveHistory(Long profileId, String type, KycStatus action, String detail) {
        ProfileKycHistory h = new ProfileKycHistory();
        h.setProfileId(profileId);
        h.setProfileType(type);
        h.setAction(action);
        h.setDetail(detail);
        historyRepo.save(h);
    }

}
