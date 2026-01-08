package com.carigo.vehicle.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.*;

public class AddVehicleRequest {

    @NotBlank(message = "Vehicle number is required")
    private String vehicleNumber;

    private String vehicleType;

    private String manufacturer;

    private String model;

    @Min(value = 1900, message = "Manufacture year seems invalid")
    @Max(value = 2100, message = "Manufacture year seems invalid")
    private Integer manufactureYear;

    private String fuelType;

    private String color;

    private String ownerName;

    private Double preDay;

    private String ownerAddress;

    @NotBlank(message = "Chassis Number is required")
    private String chassisNumber;

    @NotBlank(message = "engine Number is required")
    private String engineNumber;

    // Multipart images
    // @Size(max = 5 * 1024 * 1024, message = "Front RC image too large (max 5MB)")
    private MultipartFile rcFrontImage;

    // @Size(max = 5 * 1024 * 1024, message = "Back RC image too large (max 5MB)")
    private MultipartFile rcBackImage;

    private List<MultipartFile> vehicleImages; // multiple images
    private MultipartFile vehicleVideo;
    // Add these fields
    private Double ratePerDay;
    private Double ratePerHour;
    private String billingMode; // "PER_DAY", "PER_HOUR" or "BOTH"

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getManufactureYear() {
        return manufactureYear;
    }

    public void setManufactureYear(Integer manufactureYear) {
        this.manufactureYear = manufactureYear;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Double getPreDay() {
        return preDay;
    }

    public void setPreDay(Double preDay) {
        this.preDay = preDay;
    }

    public String getOwnerAddress() {
        return ownerAddress;
    }

    public void setOwnerAddress(String ownerAddress) {
        this.ownerAddress = ownerAddress;
    }

    public String getChassisNumber() {
        return chassisNumber;
    }

    public void setChassisNumber(String chassisNumber) {
        this.chassisNumber = chassisNumber;
    }

    public String getEngineNumber() {
        return engineNumber;
    }

    public void setEngineNumber(String engineNumber) {
        this.engineNumber = engineNumber;
    }

    public MultipartFile getRcFrontImage() {
        return rcFrontImage;
    }

    public void setRcFrontImage(MultipartFile rcFrontImage) {
        this.rcFrontImage = rcFrontImage;
    }

    public MultipartFile getRcBackImage() {
        return rcBackImage;
    }

    public void setRcBackImage(MultipartFile rcBackImage) {
        this.rcBackImage = rcBackImage;
    }

    public List<MultipartFile> getVehicleImages() {
        return vehicleImages;
    }

    public void setVehicleImages(List<MultipartFile> vehicleImages) {
        this.vehicleImages = vehicleImages;
    }

    public MultipartFile getVehicleVideo() {
        return vehicleVideo;
    }

    public void setVehicleVideo(MultipartFile vehicleVideo) {
        this.vehicleVideo = vehicleVideo;
    }

    public Double getRatePerDay() {
        return ratePerDay;
    }

    public void setRatePerDay(Double ratePerDay) {
        this.ratePerDay = ratePerDay;
    }

    public Double getRatePerHour() {
        return ratePerHour;
    }

    public void setRatePerHour(Double ratePerHour) {
        this.ratePerHour = ratePerHour;
    }

    public String getBillingMode() {
        return billingMode;
    }

    public void setBillingMode(String billingMode) {
        this.billingMode = billingMode;
    }

}
