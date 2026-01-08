package com.carigo.pament.service;

import org.springframework.stereotype.Service;

import com.carigo.pament.dto.CreateOrderRequest;
import com.carigo.pament.dto.CreateOrderResponse;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import lombok.RequiredArgsConstructor;
import org.json.JSONObject;

@Service
@RequiredArgsConstructor
public class RazorpayService {

    private final RazorpayClient razorpayClient; // Injected via @Bean config

    // ----------------------------- CREATE ORDER -----------------------------
    public CreateOrderResponse createOrder(CreateOrderRequest req) throws RazorpayException {

        // Create Razorpay order request payload
        JSONObject orderReq = new JSONObject();
        orderReq.put("amount", req.getAmount() * 100); // convert INR → paise
        orderReq.put("currency", req.getCurrency());
        orderReq.put("receipt", "BOOKING_" + req.getBookingId());
        orderReq.put("payment_capture", 1); // Auto-capture payment

        // Create order on Razorpay
        Order order = razorpayClient.orders.create(orderReq);

        // Return response DTO
        return new CreateOrderResponse(
                order.get("id"),
                order.get("amount"),
                order.get("currency"));
    }
}
