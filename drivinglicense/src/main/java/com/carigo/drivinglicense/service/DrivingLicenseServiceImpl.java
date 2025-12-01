package com.carigo.drivinglicense.service;

import com.carigo.drivinglicense.client.Exitdl;
import com.carigo.drivinglicense.dto.AddDrivingLicenseRequest;
import com.carigo.drivinglicense.dto.DrivingLicenseDto;
import com.carigo.drivinglicense.exception.ResourceNotFoundException;
import com.carigo.drivinglicense.helper.KycStatus;
import com.carigo.drivinglicense.mapper.DrivingLicenseMapper;
import com.carigo.drivinglicense.model.DlKycHistory;
import com.carigo.drivinglicense.model.DrivingLicenseEntity;
import com.carigo.drivinglicense.repository.DlKycHistoryRepository;
import com.carigo.drivinglicense.repository.DrivingLicenseRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class DrivingLicenseServiceImpl implements DrivingLicenseService {

    private final DrivingLicenseRepository repo;
    private final DlKycHistoryRepository historyRepo;
    private final DlExternalClient externalClient;
    private final CloudinaryService cloudinaryService;
    private final Exitdl exitdl;

    // ---------------------------------------------------------
    // CREATE DL
    // ---------------------------------------------------------
    @Override
    public DrivingLicenseDto add(Long id, AddDrivingLicenseRequest req) throws IOException {

        log.info("Creating DL for number: {}", req.getDlNumber());

        // verify user exists
        if (!exitdl.verifyUserID(id)) {
            throw new ResourceNotFoundException("user id not present in user profile");
        }

        // verify dl exists in profile
        if (!exitdl.dlIsPresent(req.getDlNumber())) {
            throw new ResourceNotFoundException("dl is not present in profile first add dl in profile");
        }

        // check duplicate dl
        if (repo.findByDlNumber(req.getDlNumber()).isPresent()) {
            throw new RuntimeException("Driving License already exists");
        }

        // map to entity
        DrivingLicenseEntity dlnew = DrivingLicenseMapper.toEntity(req);
        dlnew.setUserId(id); // ❤️ Set user id from URL

        // upload images
        if (req.getFrontImage() != null && !req.getFrontImage().isEmpty()) {
            dlnew.setFrontImageUrl(cloudinaryService.upload(req.getFrontImage(), "dl/front"));
        }

        if (req.getBackImage() != null && !req.getBackImage().isEmpty()) {
            dlnew.setBackImageUrl(cloudinaryService.upload(req.getBackImage(), "dl/back"));
        }

        // save
        repo.save(dlnew);

        return DrivingLicenseMapper.toDto(dlnew);
    }

    // ---------------------------------------------------------
    // UPDATE DL
    // ---------------------------------------------------------
    @Override
    public DrivingLicenseDto update(String id, AddDrivingLicenseRequest req) throws IOException {

        DrivingLicenseEntity dl = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DL not found"));

        // Update fields only when provided
        if (req.getFullName() != null)
            dl.setFullName(req.getFullName());
        if (req.getFatherName() != null)
            dl.setFatherName(req.getFatherName());
        if (req.getDateOfBirth() != null)
            dl.setDateOfBirth(req.getDateOfBirth());
        if (req.getBloodGroup() != null)
            dl.setBloodGroup(req.getBloodGroup());
        if (req.getAddress() != null)
            dl.setAddress(req.getAddress());
        if (req.getIssueDate() != null)
            dl.setIssueDate(req.getIssueDate());
        if (req.getExpiryDate() != null)
            dl.setExpiryDate(req.getExpiryDate());
        if (req.getVehicleClasses() != null)
            dl.setVehicleClasses(req.getVehicleClasses());

        // Replace images
        if (req.getFrontImage() != null) {
            dl.setFrontImageUrl(cloudinaryService.upload(req.getFrontImage(), "dl/front"));
        }

        if (req.getBackImage() != null) {
            dl.setBackImageUrl(cloudinaryService.upload(req.getBackImage(), "dl/back"));
        }

        repo.save(dl);
        return DrivingLicenseMapper.toDto(dl);
    }

    // ---------------------------------------------------------
    // GET BY ID
    // ---------------------------------------------------------
    @Override
    public DrivingLicenseDto getById(String id) {
        DrivingLicenseEntity dl = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driving License not found"));
        return DrivingLicenseMapper.toDto(dl);
    }

    // ---------------------------------------------------------
    // GET BY USER ID
    // ---------------------------------------------------------
    @Override
    public DrivingLicenseDto getByUserId(Long id) {
        DrivingLicenseEntity dl = repo.findByUserId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driving license not found"));
        return DrivingLicenseMapper.toDto(dl);
    }

    // ---------------------------------------------------------
    // GET BY DL NUMBER
    // ---------------------------------------------------------
    @Override
    public DrivingLicenseDto getByDlNumber(String dlNumber) {
        DrivingLicenseEntity dl = repo.findByDlNumber(dlNumber)
                .orElseThrow(() -> new ResourceNotFoundException("DL not found"));
        return DrivingLicenseMapper.toDto(dl);
    }

    // ---------------------------------------------------------
    // LIST DL
    // ---------------------------------------------------------
    @Override
    public List<DrivingLicenseDto> list(int page, int size) {
        return repo.findAll(PageRequest.of(page, size))
                .map(DrivingLicenseMapper::toDto)
                .toList();
    }

    // ---------------------------------------------------------
    // DELETE DL
    // ---------------------------------------------------------
    @Override
    public void delete(String id) {
        DrivingLicenseEntity dl = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DL not found"));
        repo.delete(dl);
    }

    // ---------------------------------------------------------
    // VERIFY KYC
    // ---------------------------------------------------------
    @Override
    public DrivingLicenseDto verify(String id, Map<String, Object> external) {

        DrivingLicenseEntity dl = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DL not found"));

        boolean match = false;

        String extName = (String) external.get("fullName");
        String extDob = (String) external.get("dob");
        String extDlNum = (String) external.get("dlNumber");

        if (extDlNum != null && extDlNum.equalsIgnoreCase(dl.getDlNumber()))
            match = true;
        if (extName != null && dl.getFullName() != null &&
                extName.equalsIgnoreCase(dl.getFullName()))
            match = true;
        if (extDob != null && dl.getDateOfBirth() != null &&
                extDob.equalsIgnoreCase(dl.getDateOfBirth()))
            match = true;

        // Save history
        DlKycHistory history = new DlKycHistory();
        history.setDlId(dl.getDlId());
        history.setAction(match ? "VERIFIED" : "FAILED");
        history.setDetail(match ? "DL matched with external API" : "DL mismatch");
        historyRepo.save(history);

        dl.setKycStatus(match ? KycStatus.VERIFIED : KycStatus.FAILED);
        repo.save(dl);

        return DrivingLicenseMapper.toDto(dl);
    }
}
