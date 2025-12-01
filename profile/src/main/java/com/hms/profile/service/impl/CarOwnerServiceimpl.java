package com.hms.profile.service.impl;

import com.hms.profile.client.VehicleUserId;
import com.hms.profile.dto.CarOwnerRequestDTO;
import com.hms.profile.dto.CarOwnerResponseDTO;
import com.hms.profile.dto.UserDTO;
import com.hms.profile.exception.ResourceNotFoundException;
import com.hms.profile.mapper.CarOwnerMapper;
import com.hms.profile.model.CarOwner;
import com.hms.profile.repository.CarOwnerRepository;
import com.hms.profile.service.CarOwnerService;
import com.hms.profile.service.CloudinaryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CarOwnerServiceimpl implements CarOwnerService {

    private final CarOwnerRepository repo;
    private final CloudinaryService cloudinary;

    // ---------------------------------------------------------------------
    // 1. CREATE OWNER
    // ---------------------------------------------------------------------
    @Override
    public void createOwner(UserDTO dto) {

        repo.findByEmail(dto.getEmail())
                .ifPresent(o -> {
                    throw new IllegalArgumentException("Email already exists");
                });
        CarOwner proflie = new CarOwner();
        proflie.setName(dto.getName());
        proflie.setEmail(dto.getEmail());
        proflie.setId(dto.getId());
        repo.save(proflie);

    }

    // ---------------------------------------------------------------------
    // 2. GET PROFILE
    // ---------------------------------------------------------------------
    @Override
    public CarOwnerResponseDTO getOwnerById(Long id) {
        return CarOwnerMapper.toDto(getEntity(id));
    }

    @Override
    public CarOwnerResponseDTO getOwnerByEmail(String email) {
        CarOwner owner = repo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found"));
        return CarOwnerMapper.toDto(owner);
    }

    // ---------------------------------------------------------------------
    // 3. UPDATE PROFILE
    // ---------------------------------------------------------------------
    @Override
    public CarOwnerResponseDTO updateOwner(Long id, CarOwnerRequestDTO dto) {

        CarOwner owner = getEntity(id);

        CarOwnerMapper.updateEntity(owner, dto);
        CarOwner saved = repo.save(owner);

        return CarOwnerMapper.toDto(saved);
    }

    // ---------------------------------------------------------------------
    // 4. KYC DOCUMENT UPLOADS
    // ---------------------------------------------------------------------
    @Override
    public CarOwnerResponseDTO uploadAadhaarFront(Long id, MultipartFile file) throws IOException {

        CarOwner owner = getEntity(id);

        String url = cloudinary.uploadFile(file, "owner/" + id + "/aadhaar/front");
        owner.setAadhaarFrontUrl(url);

        return save(owner);
    }

    @Override
    public CarOwnerResponseDTO uploadAadhaarBack(Long id, MultipartFile file) throws IOException {

        CarOwner owner = getEntity(id);

        String url = cloudinary.uploadFile(file, "owner/" + id + "/aadhaar/back");
        owner.setAadhaarBackUrl(url);

        return save(owner);
    }

    @Override
    public CarOwnerResponseDTO uploadSelfie(Long id, MultipartFile file) throws IOException {

        CarOwner owner = getEntity(id);

        String url = cloudinary.uploadFile(file, "owner/" + id + "/selfie");
        owner.setSelfieUrl(url);

        return save(owner);
    }

    @Override
    public CarOwnerResponseDTO uploadDrivingLicense(Long id, MultipartFile file) throws IOException {

        CarOwner owner = getEntity(id);

        String url = cloudinary.uploadFile(file, "owner/" + id + "/dl");
        owner.setDrivingLicenseNumber(url);

        return save(owner);
    }

    // ---------------------------------------------------------------------
    // 5. KYC WORKFLOW
    // ---------------------------------------------------------------------
    @Override
    public CarOwnerResponseDTO verifyKyc(Long id) {

        CarOwner owner = getEntity(id);

        if (owner.getAadhaarFrontUrl() == null ||
                owner.getAadhaarBackUrl() == null ||
                owner.getSelfieUrl() == null ||
                owner.getDrivingLicenseNumber() == null) {

            owner.setKycStatus("INCOMPLETE");
            return save(owner);
        }

        owner.setKycStatus("VERIFIED");
        return save(owner);
    }

    @Override
    public CarOwnerResponseDTO markKycRejected(Long id, String reason) {

        CarOwner owner = getEntity(id);
        owner.setKycStatus("REJECTED");

        log.warn("Owner KYC Rejected: {} | Reason: {}", id, reason);

        return save(owner);
    }

    // ---------------------------------------------------------------------
    // 6. WALLET OPERATIONS
    // ---------------------------------------------------------------------
    @Override
    public CarOwnerResponseDTO addToWallet(Long id, double amount) {

        CarOwner owner = getEntity(id);
        owner.setWalletBalance(owner.getWalletBalance() + amount);

        return save(owner);
    }

    @Override
    public CarOwnerResponseDTO deductFromWallet(Long id, double amount) {

        CarOwner owner = getEntity(id);

        if (owner.getWalletBalance() < amount) {
            throw new IllegalArgumentException("Insufficient wallet balance");
        }

        owner.setWalletBalance(owner.getWalletBalance() - amount);
        return save(owner);
    }

    @Override
    public CarOwnerResponseDTO withdrawToBank(Long id, double amount) {

        CarOwner owner = getEntity(id);

        if (owner.getWalletBalance() < amount) {
            throw new IllegalArgumentException("Insufficient wallet balance");
        }

        owner.setWalletBalance(owner.getWalletBalance() - amount);
        owner.setTotalEarnings(owner.getTotalEarnings() + amount); // optional: track withdrawals

        return save(owner);
    }

    @Override
    public Double getWalletBalance(Long id) {
        return getEntity(id).getWalletBalance();
    }

    // ---------------------------------------------------------------------
    // 7. BANK DETAILS UPDATE
    // ---------------------------------------------------------------------
    @Override
    public CarOwnerResponseDTO updateBankDetails(Long id, String name, String accNum, String ifsc) {

        CarOwner owner = getEntity(id);

        owner.setAccountHolderName(name);
        owner.setBankAccountNumber(accNum);
        owner.setIfscCode(ifsc);

        return save(owner);
    }

    // ---------------------------------------------------------------------
    // 8. RATING + TRIP COUNT (called from booking service)
    // ---------------------------------------------------------------------
    @Override
    public CarOwnerResponseDTO updateRating(Long id, double rating) {

        CarOwner owner = getEntity(id);

        owner.setRating((owner.getRating() + rating) / 2.0);
        return save(owner);
    }

    @Override
    public void incrementCompletedTrips(Long id) {

        CarOwner owner = getEntity(id);
        owner.setTotalTripsCompleted(owner.getTotalTripsCompleted() + 1);

        repo.save(owner);
    }

    // ---------------------------------------------------------------------
    // 9. LIST + SEARCH
    // ---------------------------------------------------------------------
    @Override
    public List<CarOwnerResponseDTO> listOwners(int page, int size) {

        return repo.findAll(PageRequest.of(page, size))
                .stream()
                .map(CarOwnerMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CarOwnerResponseDTO> searchByName(String name) {

        return repo.findByNameContainingIgnoreCase(name)
                .stream()
                .map(CarOwnerMapper::toDto)
                .collect(Collectors.toList());
    }

    // ---------------------------------------------------------------------
    // 10. DELETE OWNER
    // ---------------------------------------------------------------------
    @Override
    public void deleteOwner(Long id) {

        CarOwner owner = getEntity(id);
        repo.delete(owner);
    }

    @Override
    public void deleteOwnerByUserId(Long userId) {
        CarOwner owner = repo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found"));

        repo.delete(owner);
    }

    // ---------------------------------------------------------------------
    // Helper Methods
    // ---------------------------------------------------------------------
    private CarOwner getEntity(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found"));
    }

    private CarOwnerResponseDTO save(CarOwner owner) {
        return CarOwnerMapper.toDto(repo.save(owner));
    }

    @Override
    public CarOwnerResponseDTO getUserByUserId(Long userId) {
        CarOwner owner = repo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found"));
        return CarOwnerMapper.toDto(owner);
    }

    @Override
    public Boolean verifyUser(Long id) {
        if (!repo.existsById(id)) {
            log.error("Owner not found for ID: {}", id);
            throw new ResourceNotFoundException("Owner not found");
        }
        return true;
    }

    // public Boolean verifyVehicleNumber(String number) {
    // if (!repo.existsByVehicleNumber(number)) {
    // log.error("Vehiicle number is no present" + number);
    // throw new ResourceNotFoundException("Vehiiicle is no present :" + number,
    // "First add vihicle in profile page");
    // }
    // return true;
    // }

}
