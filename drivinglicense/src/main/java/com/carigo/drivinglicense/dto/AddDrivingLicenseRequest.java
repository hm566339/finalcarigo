package com.carigo.drivinglicense.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AddDrivingLicenseRequest {

    @NotBlank
    private String dlNumber;

    private String fullName;
    private String fatherName;
    private String dateOfBirth;
    private String bloodGroup;
    private String address;
    private String issueDate;
    private String expiryDate;
    private String vehicleClasses;

    // @Size(max = 5 * 1024 * 1024, message = "Back RC image too large (max 5MB)")
    private MultipartFile frontImage;

    // @Size(max = 5 * 1024 * 1024, message = "Back RC image too large (max 5MB)")
    private MultipartFile backImage;
}
