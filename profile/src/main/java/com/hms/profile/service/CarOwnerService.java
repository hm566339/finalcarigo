package com.hms.profile.service;

import com.hms.profile.dto.CarOwnerRequestDTO;
import com.hms.profile.dto.CarOwnerResponseDTO;
import com.hms.profile.dto.UserDTO;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CarOwnerService {

    // ----------------------------------
    // 1) CREATE OWNER ACCOUNT
    // ----------------------------------
    void createOwner(UserDTO dto);

    // ----------------------------------
    // 2) GET PROFILE
    // ----------------------------------
    CarOwnerResponseDTO getOwnerById(Long id);

    CarOwnerResponseDTO getOwnerByEmail(String email);

    // ----------------------------------
    // 3) UPDATE PROFILE
    // ----------------------------------
    CarOwnerResponseDTO updateOwner(Long id, CarOwnerRequestDTO dto);

    // ----------------------------------
    // 4) KYC DOCUMENT UPLOADS
    // ----------------------------------
    CarOwnerResponseDTO uploadAadhaarFront(Long id, MultipartFile file) throws IOException;

    CarOwnerResponseDTO uploadAadhaarBack(Long id, MultipartFile file) throws IOException;

    CarOwnerResponseDTO uploadSelfie(Long id, MultipartFile file) throws IOException;

    CarOwnerResponseDTO uploadDrivingLicense(Long id, MultipartFile file) throws IOException;

    // ----------------------------------
    // 5) KYC VERIFICATION WORKFLOW
    // ----------------------------------
    CarOwnerResponseDTO verifyKyc(Long id); // OCR + Selfie match + DL check

    CarOwnerResponseDTO markKycRejected(Long id, String reason);

    // ----------------------------------
    // 6) OWNER WALLET OPERATIONS
    // ----------------------------------
    CarOwnerResponseDTO addToWallet(Long id, double amount);

    CarOwnerResponseDTO deductFromWallet(Long id, double amount);

    CarOwnerResponseDTO withdrawToBank(Long id, double amount);

    Double getWalletBalance(Long id);

    // ----------------------------------
    // 7) BANK ACCOUNT MANAGEMENT
    // ----------------------------------
    CarOwnerResponseDTO updateBankDetails(
            Long id,
            String accountHolderName,
            String accountNumber,
            String ifscCode);

    // ----------------------------------
    // 8) RATING SYSTEM (Bookings se call hoga)
    // ----------------------------------
    CarOwnerResponseDTO updateRating(Long id, double rating);

    void incrementCompletedTrips(Long id);

    // ----------------------------------
    // 9) OWNER LISTING
    // ----------------------------------
    List<CarOwnerResponseDTO> listOwners(int page, int size);

    List<CarOwnerResponseDTO> searchByName(String name);

    // ----------------------------------
    // 10) DELETE OWNER ACCOUNT
    // ----------------------------------
    void deleteOwner(Long id);

    CarOwnerResponseDTO getUserByUserId(Long userId);

    void deleteOwnerByUserId(Long userId);

    Boolean verifyUser(Long id);

    // Boolean verifyVehicleNumber(String number);

}
