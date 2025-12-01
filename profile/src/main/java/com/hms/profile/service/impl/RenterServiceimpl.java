package com.hms.profile.service.impl;

import com.hms.profile.dto.RenterRequestDTO;
import com.hms.profile.dto.RenterResponseDTO;
import com.hms.profile.dto.UserDTO;
import com.hms.profile.exception.ResourceNotFoundException;
import com.hms.profile.mapper.RenterMapper;
import com.hms.profile.model.Renter;
import com.hms.profile.repository.RenterRepository;
import com.hms.profile.service.CloudinaryService;
import com.hms.profile.service.RenterService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RenterServiceimpl implements RenterService {

    private final RenterRepository renterRepository;
    private final CloudinaryService cloudinaryService;

    // -------------------------------------------------------------------
    // CREATE RENTER
    // -------------------------------------------------------------------
    @Override
    public void createRenter(UserDTO dto) {

        renterRepository.findByEmail(dto.getEmail())
                .ifPresent(r -> {
                    throw new IllegalArgumentException("Email already exists");
                });
        Renter renter = new Renter();
        renter.setName(dto.getName());
        renter.setEmail(dto.getEmail());
        renter.setId(dto.getId());
        renterRepository.save(renter);
    }

    // -------------------------------------------------------------------
    // GET BY ID
    // -------------------------------------------------------------------
    @Override
    public RenterResponseDTO getRenterById(Long id) {

        Renter renter = renterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Renter not found"));

        return RenterMapper.toDto(renter);
    }

    // -------------------------------------------------------------------
    // GET BY EMAIL
    // -------------------------------------------------------------------
    @Override
    public RenterResponseDTO getRenterByEmail(String email) {

        Renter renter = renterRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Renter not found"));

        return RenterMapper.toDto(renter);
    }

    // -------------------------------------------------------------------
    // UPDATE RENTER
    // -------------------------------------------------------------------
    @Override
    public RenterResponseDTO updateRenter(Long id, RenterRequestDTO dto) {

        Renter renter = renterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Renter not found"));

        RenterMapper.updateEntity(renter, dto);

        Renter saved = renterRepository.save(renter);
        return RenterMapper.toDto(saved);
    }

    // -------------------------------------------------------------------
    // KYC FILE UPLOADS
    // -------------------------------------------------------------------
    @Override
    public RenterResponseDTO uploadAadhaarFront(Long id, MultipartFile file) throws IOException {

        Renter renter = getEntity(id);

        String url = cloudinaryService.uploadFile(file, "renter/" + id + "/aadhaar/front");
        renter.setAadhaarFrontUrl(url);

        return saveAndReturn(renter);
    }

    @Override
    public RenterResponseDTO uploadAadhaarBack(Long id, MultipartFile file) throws IOException {

        Renter renter = getEntity(id);

        String url = cloudinaryService.uploadFile(file, "renter/" + id + "/aadhaar/back");
        renter.setAadhaarBackUrl(url);

        return saveAndReturn(renter);
    }

    @Override
    public RenterResponseDTO uploadSelfie(Long id, MultipartFile file) throws IOException {

        Renter renter = getEntity(id);

        String url = cloudinaryService.uploadFile(file, "renter/" + id + "/selfie");
        renter.setSelfieUrl(url);

        return saveAndReturn(renter);
    }

    @Override
    public RenterResponseDTO uploadDrivingLicense(Long id, MultipartFile file) throws IOException {

        Renter renter = getEntity(id);

        String url = cloudinaryService.uploadFile(file, "renter/" + id + "/dl");
        renter.setDrivingLicenseNumber(url);

        return saveAndReturn(renter);
    }

    // -------------------------------------------------------------------
    // KYC VERIFICATION LOGIC
    // -------------------------------------------------------------------
    @Override
    public RenterResponseDTO verifyKyc(Long id) {

        Renter renter = getEntity(id);

        // Basic validation (advance: OCR + face match)
        if (renter.getAadhaarFrontUrl() == null ||
                renter.getAadhaarBackUrl() == null ||
                renter.getSelfieUrl() == null ||
                renter.getDrivingLicenseNumber() == null) {

            renter.setKycStatus("INCOMPLETE");
            return saveAndReturn(renter);
        }

        renter.setKycStatus("VERIFIED");
        return saveAndReturn(renter);
    }

    @Override
    public RenterResponseDTO markKycRejected(Long id, String reason) {

        Renter renter = getEntity(id);
        renter.setKycStatus("REJECTED");

        log.warn("KYC rejected for renter {} : {}", id, reason);

        return saveAndReturn(renter);
    }

    // -------------------------------------------------------------------
    // DELETE RENTER
    // -------------------------------------------------------------------
    @Override
    public void deleteRenter(Long id) {

        Renter renter = getEntity(id);
        renterRepository.delete(renter);
    }

    @Override
    public void deleteRenterByUserId(Long userId) {

        Renter renter = renterRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Renter not found"));

        renterRepository.delete(renter);
    }

    // -------------------------------------------------------------------
    // LIST & SEARCH
    // -------------------------------------------------------------------
    @Override
    public List<RenterResponseDTO> listRenters(int page, int size) {

        return renterRepository.findAll(PageRequest.of(page, size))
                .stream()
                .map(RenterMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RenterResponseDTO> searchByName(String name) {

        return renterRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(RenterMapper::toDto)
                .collect(Collectors.toList());
    }

    // -------------------------------------------------------------------
    // RATING & TRIP COUNT
    // -------------------------------------------------------------------
    @Override
    public RenterResponseDTO updateRating(Long id, double rating) {

        Renter renter = getEntity(id);

        renter.setRating((renter.getRating() + rating) / 2.0);

        return saveAndReturn(renter);
    }

    @Override
    public void incrementTripCount(Long id) {

        Renter renter = getEntity(id);
        renter.setTotalTrips(renter.getTotalTrips() + 1);

        renterRepository.save(renter);
    }

    @Override
    public Boolean isPresentDl(String dl) {
        Boolean present = renterRepository.existsByDrivingLicenseNumber(dl);
        return present;
    }

    // -------------------------------------------------------------------
    // HELPER METHODS
    // -------------------------------------------------------------------
    private Renter getEntity(Long id) {
        return renterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Renter not found"));
    }

    private RenterResponseDTO saveAndReturn(Renter renter) {
        Renter saved = renterRepository.save(renter);
        return RenterMapper.toDto(saved);
    }

    // --------------------------------------------------------------------
    // boolen user find
    // --------------------------------------------------------------------
    public Boolean exitUserID(Long id) {
        return renterRepository.existsById(id);
    }

}
