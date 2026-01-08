package com.carigo.pament.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VerifyPaymentResponse {

    private String message;
    private String paymentId;

}