package com.carigo.vehicle.dto;

public class UserAndVehicleVerify {

    private Long ownerId;
    private String vehicleId;
    private Double preDay;

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Double getPreDay() {
        return preDay;
    }

    public void setPreDay(Double preDay) {
        this.preDay = preDay;
    }

}
