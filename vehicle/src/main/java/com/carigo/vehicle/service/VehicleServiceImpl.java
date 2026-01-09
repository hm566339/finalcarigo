package com.carigo.vehicle.service;

import com.carigo.vehicle.client.UserVerify;
import com.carigo.vehicle.dto.AddVehicleRequest;
import com.carigo.vehicle.dto.UserAndVehicleVerify;
import com.carigo.vehicle.dto.VehicleDto;
import com.carigo.vehicle.dto.VehicleStats;
import com.carigo.vehicle.exception.ResourceNotFoundException;
import com.carigo.vehicle.helper.KycStatus;
import com.carigo.vehicle.helper.VehicleStatus;
import com.carigo.vehicle.mapper.VehicleMapper;
import com.carigo.vehicle.model.KycHistory;
import com.carigo.vehicle.model.VehicleEntity;
import com.carigo.vehicle.repository.KycHistoryRepository;
import com.carigo.vehicle.repository.VehicleRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository repository;
    private final CloudinaryService cloudinaryService;
    private final VerificationService verificationService;
    private final KycHistoryRepository historyRepository;
    private final UserVerify userVerify;

    @Transactional
    public void createVehicleMultipart(Long userId, AddVehicleRequest req) throws IOException {

        if (repository.existsByVehicleNumber(req.getVehicleNumber())) {
            throw new RuntimeException("Vehicle already exists: " + req.getVehicleNumber());
        }

        if (!userVerify.verifyUserExists(userId)) {
            throw new RuntimeException("User not found with id: " + userId);
        }

        VehicleEntity entity = VehicleMapper.toEntity(req);
        entity.setUserId(userId);

        String rcFolder = "vehicles/" + userId + "/rc";
        String mediaFolder = "vehicles/" + userId + "/media";

        // ===== RC FRONT =====
        if (req.getRcFrontImage() != null && !req.getRcFrontImage().isEmpty()) {
            entity.setRcFrontImageUrl(
                    cloudinaryService.uploadFile(req.getRcFrontImage(), rcFolder));
        }

        // ===== RC BACK =====
        if (req.getRcBackImage() != null && !req.getRcBackImage().isEmpty()) {
            entity.setRcBackImageUrl(
                    cloudinaryService.uploadFile(req.getRcBackImage(), rcFolder));
        }

        // ===== VEHICLE IMAGES =====
        if (req.getVehicleImages() != null && !req.getVehicleImages().isEmpty()) {
            List<String> imageUrls = new java.util.ArrayList<>();

            for (var img : req.getVehicleImages()) {
                if (!img.isEmpty()) {
                    imageUrls.add(
                            cloudinaryService.uploadFile(img, mediaFolder));
                }
            }
            entity.setVehicleImageUrls(imageUrls);
        }

        // ===== VEHICLE VIDEO =====
        if (req.getVehicleVideo() != null && !req.getVehicleVideo().isEmpty()) {
            entity.setVehicleVideoUrl(
                    cloudinaryService.uploadFile(req.getVehicleVideo(), mediaFolder));
        }

        repository.save(entity);
    }

    // GET BY ID
    @Override
    public VehicleDto getVehicleById(String id) {
        VehicleEntity entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        return VehicleMapper.toDto(entity);
    }

    // GET BY USER ID
    @Override
    @Cacheable(value = "vehicles", key = "#vehicleNumber")
    public List<VehicleDto> getByUserId(Long id) {
        List<VehicleEntity> vehicleEntity = repository.findByUserId(id);
        return vehicleEntity
                .stream()
                .map(VehicleMapper::toDto)
                .toList();

    }

    public VehicleDto getByVehicleNumber(String number) {
        if (number == null || number.isBlank()) {
            throw new IllegalArgumentException("Vehicle number must not be empty");
        }
        VehicleEntity entity = repository.findByVehicleNumber(number)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle is not present"));

        return VehicleMapper.toDto(entity);
    }

    // VERIFY KYC
    @Override
    @Transactional
    @CacheEvict(value = "vehicles", allEntries = true)
    public VehicleDto verifyVehicle(String id, Object externalApiResponse) {

        VehicleEntity entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        boolean matched = verificationService.matchWithExternal(entity, externalApiResponse);

        entity.setKycStatus(matched ? KycStatus.VERIFIED : KycStatus.FAILED);
        repository.save(entity);

        return VehicleMapper.toDto(entity);
    }

    @Override
    @Transactional
    @CacheEvict(value = "vehicles", allEntries = true)
    public VehicleDto updateVehicle(String id, AddVehicleRequest request) throws IOException {

        VehicleEntity entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        VehicleMapper.updateEntity(entity, request);

        String folder = "vehicles/" + entity.getVehicleId() + "/media";

        // ===== UPDATE VEHICLE IMAGES =====
        if (request.getVehicleImages() != null && !request.getVehicleImages().isEmpty()) {

            if (entity.getVehicleImageUrls() != null) {
                entity.getVehicleImageUrls()
                        .forEach(cloudinaryService::deleteFile);
            }

            List<String> newImages = new java.util.ArrayList<>();
            for (var img : request.getVehicleImages()) {
                if (!img.isEmpty()) {
                    newImages.add(
                            cloudinaryService.uploadFile(img, folder));
                }
            }
            entity.setVehicleImageUrls(newImages);
        }

        // ===== UPDATE VEHICLE VIDEO =====
        if (request.getVehicleVideo() != null && !request.getVehicleVideo().isEmpty()) {

            cloudinaryService.deleteFile(entity.getVehicleVideoUrl());

            entity.setVehicleVideoUrl(
                    cloudinaryService.uploadFile(request.getVehicleVideo(), folder));
        }

        return VehicleMapper.toDto(repository.save(entity));
    }

    @Override
    @Transactional
    @CacheEvict(value = "vehicles", allEntries = true)
    public void deleteVehicle(String id) {

        VehicleEntity entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        // RC images
        cloudinaryService.deleteFile(entity.getRcFrontImageUrl());
        cloudinaryService.deleteFile(entity.getRcBackImageUrl());

        // Vehicle images
        if (entity.getVehicleImageUrls() != null) {
            entity.getVehicleImageUrls()
                    .forEach(cloudinaryService::deleteFile);
        }

        // Vehicle video
        cloudinaryService.deleteFile(entity.getVehicleVideoUrl());

        repository.delete(entity);
    }

    // LIST
    @Override
    public List<VehicleDto> listAllVehicles(int page, int size) {

        return repository.findAll(PageRequest.of(page, size))
                .stream()
                .map(VehicleMapper::toDto)
                .collect(Collectors.toList());
    }

    // Helpers
    private void uploadImages(AddVehicleRequest req, VehicleEntity e, String folder) throws IOException {

        if (req.getRcFrontImage() != null && !req.getRcFrontImage().isEmpty()) {
            e.setRcFrontImageUrl(cloudinaryService.uploadFile(req.getRcFrontImage(), folder));
        }

        if (req.getRcBackImage() != null && !req.getRcBackImage().isEmpty()) {
            e.setRcBackImageUrl(cloudinaryService.uploadFile(req.getRcBackImage(), folder));
        }
    }

    private void replaceImages(AddVehicleRequest req, VehicleEntity e, String folder) throws IOException {

        if (req.getRcFrontImage() != null && !req.getRcFrontImage().isEmpty()) {
            cloudinaryService.deleteFile(e.getRcFrontImageUrl());
            e.setRcFrontImageUrl(cloudinaryService.uploadFile(req.getRcFrontImage(), folder));
        }

        if (req.getRcBackImage() != null && !req.getRcBackImage().isEmpty()) {
            cloudinaryService.deleteFile(e.getRcBackImageUrl());
            e.setRcBackImageUrl(cloudinaryService.uploadFile(req.getRcBackImage(), folder));
        }
    }

    @Override
    public Boolean exitVehicleNumber(String id) {
        Boolean exit = repository.existsByVehicleNumber(id);
        return exit;
    }

    @Override
    public Boolean verifyUserAndVehicle(UserAndVehicleVerify dto) {

        // 1) Check vehicle exists
        VehicleEntity vehicle = repository.findByVehicleNumber(dto.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        // 2) Check owner exists
        if (!repository.existsByUserId(dto.getOwnerId())) {
            throw new ResourceNotFoundException("Owner not found");
        }

        // 3) Check if vehicle belongs to the owner
        if (!vehicle.getUserId().equals(dto.getOwnerId())) {
            throw new ResourceNotFoundException("Vehicle does not belong to this owner");
        }

        // 4) NULL-SAFE preDay comparison (FIXED)
        if (!Objects.equals(vehicle.getPreDay(), dto.getPreDay())) {
            throw new ResourceNotFoundException("Price does not match with vehicle per day");
        }

        return true;
    }

    @Transactional
    @Override
    public VehicleDto adminKycOverride(String vehicleId, KycStatus status, String reason) {

        VehicleEntity vehicle = repository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        vehicle.setKycStatus(status);
        repository.save(vehicle);

        KycHistory h = new KycHistory();
        h.setVehicleId(vehicleId);
        h.setAction(status.name());
        h.setDetail("Admin override: " + reason);
        historyRepository.save(h);

        return VehicleMapper.toDto(vehicle);
    }

    @Transactional
    @Override
    public VehicleDto changeStatus(String id, VehicleStatus status) {

        VehicleEntity vehicle = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        vehicle.setStatus(status);
        return VehicleMapper.toDto(repository.save(vehicle));
    }

    @Override
    public long countAllVehicles() {
        return repository.count();
    }

    @Override
    public long countActiveVehicles() {
        return repository.countByStatus(VehicleStatus.ACTIVE);
    }

    @Override
    public long countBlockedVehicles() {
        return repository.countByStatus(VehicleStatus.BLOCKED);
    }

    @Override
    public long countPendingKycVehicles() {
        return repository.countByKycStatus(KycStatus.PENDING);
    }

    @Override
    public long countInsuranceExpiringSoon() {
        return repository.countByInsuranceExpiryDateBefore(
                LocalDate.now().plusDays(30));
    }

    @Override
    public VehicleStats getVehicleStats(Long ownerId) {
        long total = repository.countByUserId(ownerId);
        long active = repository.countByUserIdAndStatus(
                ownerId, VehicleStatus.ACTIVE);

        long inactive = total - active;

        return VehicleStats.builder()
                .total(total)
                .active(active)
                .inactive(inactive)
                .build();
    }

    @Override
    public long countInsuranceExpiring(Long ownerId) {

        LocalDate today = LocalDate.now();

        return repository
                .countByUserIdAndInsuranceExpiryDateAfterAndInsuranceExpiryDateBefore(
                        ownerId,
                        today,
                        today.plusDays(30) // next 30 days
                );
    }
}