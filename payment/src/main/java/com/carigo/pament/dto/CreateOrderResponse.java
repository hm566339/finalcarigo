package com.carigo.pament.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateOrderResponse {

    private String orderId;
    private int amount; // Amount in paise (as returned by Razorpay)
    private String currency;

}
