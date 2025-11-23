package com.carigo.vehicle.service;

import com.carigo.vehicle.client.UserVerify;
import com.carigo.vehicle.dto.AddVehicleRequest;
import com.carigo.vehicle.dto.VehicleDto;
import com.carigo.vehicle.exception.ResourceNotFoundException;
import com.carigo.vehicle.helper.KycStatus;
import com.carigo.vehicle.mapper.VehicleMapper;
import com.carigo.vehicle.model.VehicleEntity;
import com.carigo.vehicle.repository.VehicleRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
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
    @Override
    @Transactional
    public VehicleDto addVehicle(Long id, AddVehicleRequest request) throws IOException {

        Boolean userExists = userVerify.verifyUserExists(id);
        if (userExists == null || !userExists) {
            throw new IllegalArgumentException("User with ID " + id + " does not exist.");
        }

        repository.findByVehicleNumber(request.getVehicleNumber())
                .ifPresent(v -> {
                    throw new IllegalArgumentException("Vehicle number already exists");
                });

        VehicleEntity entity = VehicleMapper.toEntity(request);

        // 🔥 REAL FIX — Set userId in the actual entity
        entity.setUserId(id);

        String folder = "vehicles/" + entity.getVehicleId() + "/rc";
        uploadImages(request, entity, folder);

        try {
            VehicleEntity saved = repository.save(entity);
            return VehicleMapper.toDto(saved);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException("Vehicle number already exists");
        }
    }

    @Override
    public String insertUserId(Long userId) {
        VehicleEntity entity = new VehicleEntity();
        entity.setUserId(userId);
        repository.save(entity);
        return "User ID inserted successfully.";

    }

    // GET BY ID
    @Override
    public VehicleDto getVehicleById(String id) {
        VehicleEntity entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        return VehicleMapper.toDto(entity);
    }

    // GET BY VEHICLE NUMBER
    @Override
    @Cacheable(value = "vehicles", key = "#vehicleNumber")
    public VehicleDto getByVehicleNumber(String vehicleNumber) {
        VehicleEntity entity = repository.findByVehicleNumber(vehicleNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

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
}
