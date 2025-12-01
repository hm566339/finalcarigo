package com.hms.profile.service;

import com.hms.profile.dto.RenterRequestDTO;
import com.hms.profile.dto.RenterResponseDTO;
import com.hms.profile.dto.UserDTO;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface RenterService {

    // ----------------------------
    // 1. CREATE ACCOUNT
    // ----------------------------
    void createRenter(UserDTO dto);

    // ----------------------------
    // 2. GET PROFILE
    // ----------------------------
    RenterResponseDTO getRenterById(Long id);

    RenterResponseDTO getRenterByEmail(String email);

    // ----------------------------
    // 3. UPDATE PROFILE
    // ----------------------------
    RenterResponseDTO updateRenter(Long id, RenterRequestDTO dto);

    // ----------------------------
    // 4. UPLOAD DOCUMENTS (KYC)
    // ----------------------------
    RenterResponseDTO uploadAadhaarFront(Long id, MultipartFile file) throws IOException;

    RenterResponseDTO uploadAadhaarBack(Long id, MultipartFile file) throws IOException;

    RenterResponseDTO uploadSelfie(Long id, MultipartFile file) throws IOException;

    // OPTIONAL → Driving License upload (if needed)
    RenterResponseDTO uploadDrivingLicense(Long id, MultipartFile file) throws IOException;

    // ----------------------------
    // 5. KYC VERIFICATION LOGIC
    // ----------------------------
    RenterResponseDTO verifyKyc(Long id); // Selfie match + Aadhaar fetch + DL check

    RenterResponseDTO markKycRejected(Long id, String reason);

    // ----------------------------
    // 6. DELETE PROFILE
    // ----------------------------
    void deleteRenter(Long id);

    void deleteRenterByUserId(Long userId);

    // ----------------------------
    // 7. LIST & SEARCH
    // ----------------------------
    List<RenterResponseDTO> listRenters(int page, int size);

    List<RenterResponseDTO> searchByName(String name);

    // ----------------------------
    // 8. RATING SYSTEM
    // ----------------------------
    RenterResponseDTO updateRating(Long id, double rating);

    // ----------------------------
    // 9. TRIP COUNTER UPDATE (Booking Service se call hoga)
    // ----------------------------
    void incrementTripCount(Long id);

    Boolean isPresentDl(String dl);

    Boolean exitUserID(Long id);

}
