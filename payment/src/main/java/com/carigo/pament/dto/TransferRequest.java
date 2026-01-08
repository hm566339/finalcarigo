package com.carigo.pament.dto;

import lombok.Data;

@Data
public class TransferRequest {

    private Long ownerId;
    private double amount; // Amount to withdraw
    private String accountNumber; // Owner's bank account
    private String ifscCode; // IFSC
    private String accountHolderName;

}
