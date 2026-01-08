package com.carigo.vehicle.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.carigo.vehicle.helper.BillingMode;
import com.carigo.vehicle.helper.KycStatus;
import com.carigo.vehicle.helper.VehicleStatus;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vehicles", indexes = {
        @Index(name = "idx_vehicle_number", columnList = "vehicle_number"),
        @Index(name = "idx_user_vehicle", columnList = "user_id, vehicle_number")
})
public class VehicleEntity {

    @Id
    @Column(name = "vehicle_id", nullable = false, updatable = false)
    private String vehicleId = UUID.randomUUID().toString();

    @Column(name = "user_id", nullable = false, length = 50)
    private Long userId;

    private Long preDay;

    // @Column(name = "vehicle_number", nullable = false, unique = true, length =
    // 20)
    private String vehicleNumber; // RC Number

    @Column(length = 30)
    private String vehicleType; // Car / Bike / Scooter

    @Column(length = 50)
    private String manufacturer;

    @Column(length = 50)
    private String model;

    private Integer manufactureYear;

    @Column(length = 20)
    private String fuelType; // Petrol / Diesel / CNG / EV

    @Column(length = 30)
    private String color;

    // Owner Details
    @Column(length = 100)
    private String ownerName;

    @Column(length = 255)
    private String ownerAddress;

    // Technical Identifiers
    @Column(length = 50)
    private String chassisNumber;

    @Column(length = 50)
    private String engineNumber;

    // RC Images (store URLs)
    @Column(length = 1000)
    private String rcFrontImageUrl;

    @Column(length = 1000)
    private String rcBackImageUrl;

    // Vehicle Images (Multiple)
    @ElementCollection
    @CollectionTable(name = "vehicle_images", joinColumns = @JoinColumn(name = "vehicle_id"))
    @Column(name = "image_url", length = 1000)
    private List<String> vehicleImageUrls;

    // Vehicle Video
    @Column(length = 1000)
    private String vehicleVideoUrl;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private KycStatus kycStatus = KycStatus.PENDING;

    @Column(name = "rate_per_day", precision = 10, scale = 2)
    private BigDecimal ratePerDay; // replaces preDay usage

    @Column(name = "rate_per_hour", precision = 10, scale = 2)
    private BigDecimal ratePerHour;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private VehicleStatus status = VehicleStatus.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(name = "billing_mode", length = 20)
    private BillingMode billingMode = BillingMode.PER_DAY;

    private LocalDate insuranceExpiryDate;

    public VehicleStatus getStatus() {
        return status;
    }

    public LocalDate getInsuranceExpiryDate() {
        return insuranceExpiryDate;
    }

    public void setInsuranceExpiryDate(LocalDate insuranceExpiryDate) {
        this.insuranceExpiryDate = insuranceExpiryDate;
    }

    // audit fields
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

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

    public Long getPreDay() {
        return preDay;
    }

    public void setPreDay(Long preDay) {
        this.preDay = preDay;
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

    public KycStatus getKycStatus() {
        return kycStatus;
    }

    public void setKycStatus(KycStatus kycStatus) {
        this.kycStatus = kycStatus;
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

    public void setStatus(VehicleStatus status2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setStatus'");
    }
}
