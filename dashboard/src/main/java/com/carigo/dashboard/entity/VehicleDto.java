package com.carigo.dashboard.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.carigo.dashboard.helper.KycStatus;

public class VehicleDto {

    private String vehicleId;

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

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

    public String getRcFrontImageUrl() {
        return rcFrontImageUrl;
    }

    public void setRcFrontImageUrl(String rcFrontImageUrl) {
        this.rcFrontImageUrl = rcFrontImageUrl;
    }

    public String getRcBackImageUrl() {
        return rcBackImageUrl;
    }

    public void setRcBackImageUrl(String rcBackImageUrl) {
        this.rcBackImageUrl = rcBackImageUrl;
    }

    public KycStatus getKycStatus() {
        return kycStatus;
    }

    public void setKycStatus(KycStatus kycStatus) {
        this.kycStatus = kycStatus;
    }

    public List<String> getVehicleImageUrls() {
        return vehicleImageUrls;
    }

    public void setVehicleImageUrls(List<String> vehicleImageUrls) {
        this.vehicleImageUrls = vehicleImageUrls;
    }

    public String getVehicleVideoUrl() {
        return vehicleVideoUrl;
    }

    public void setVehicleVideoUrl(String vehicleVideoUrl) {
        this.vehicleVideoUrl = vehicleVideoUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public BigDecimal getRatePerDay() {
        return ratePerDay;
    }

    public void setRatePerDay(BigDecimal ratePerDay) {
        this.ratePerDay = ratePerDay;
    }

    public BigDecimal getRatePerHour() {
        return ratePerHour;
    }

    public void setRatePerHour(BigDecimal ratePerHour) {
        this.ratePerHour = ratePerHour;
    }

    public BillingMode getBillingMode() {
        return billingMode;
    }

    public void setBillingMode(BillingMode billingMode) {
        this.billingMode = billingMode;
    }

    private Long userId;

    private String vehicleNumber;

    private String vehicleType;

    private String manufacturer;

    private String model;

    private Integer manufactureYear;

    private String fuelType;

    private String color;

    private String ownerName;

    private String ownerAddress;

    private String chassisNumber;

    private String engineNumber;

    private String rcFrontImageUrl;

    private String rcBackImageUrl;

    private KycStatus kycStatus;

    private List<String> vehicleImageUrls;
    private String vehicleVideoUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private BigDecimal ratePerDay;
    private BigDecimal ratePerHour;
    private BillingMode billingMode;

}
