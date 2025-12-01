package com.carigo.vehicle.service;

import com.carigo.vehicle.client.UserVerify;
import com.carigo.vehicle.dto.AddVehicleRequest;
import com.carigo.vehicle.dto.UserAndVehicleVerify;
import com.carigo.vehicle.dto.VehicleDto;
import com.carigo.vehicle.exception.ResourceNotFoundException;
import com.carigo.vehicle.helper.KycStatus;
import com.carigo.vehicle.mapper.VehicleMapper;
import com.carigo.vehicle.model.VehicleEntity;
import com.carigo.vehicle.repository.VehicleRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.tomcat.util.openssl.pem_password_cb;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository repository;
    private final CloudinaryService cloudinaryService;
    private final VerificationService verificationService;

    private final UserVerify userVerify;

    // CREATE VEHICLE
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

        String folder = "vehicles/" + userId + "/rc";

        // Upload front image
        if (req.getRcFrontImage() != null && !req.getRcFrontImage().isEmpty()) {
            String url = cloudinaryService.uploadFile(req.getRcFrontImage(), folder);
            entity.setRcFrontImageUrl(url);
        }

        // Upload back image
        if (req.getRcBackImage() != null && !req.getRcBackImage().isEmpty()) {
            String url = cloudinaryService.uploadFile(req.getRcBackImage(), folder);
            entity.setRcBackImageUrl(url);
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

    // UPDATE VEHICLE
    @Override
    @Transactional
    @CacheEvict(value = "vehicles", allEntries = true)
    public VehicleDto updateVehicle(String id, AddVehicleRequest request) throws IOException {

        VehicleEntity entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        // Update fields
        VehicleMapper.updateEntity(entity, request);

        String folder = "vehicles/" + entity.getVehicleId() + "/rc";
        replaceImages(request, entity, folder);

        return VehicleMapper.toDto(repository.save(entity));
    }

    // DELETE
    @Override
    @Transactional
    @CacheEvict(value = "vehicles", allEntries = true)
    public void deleteVehicle(String id) {

        VehicleEntity entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        cloudinaryService.deleteFile(entity.getRcFrontImageUrl());
        cloudinaryService.deleteFile(entity.getRcBackImageUrl());

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
        if (!vehicle.getPreDay().equals(dto.getPreDay())) {
            throw new ResourceNotFoundException("Price is not equal to the vehicle per Day");
        }

        return true;
    }

}