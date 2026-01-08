package com.carigo.pament.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.carigo.pament.dto.CreateOrderRequest;
import com.carigo.pament.dto.CreateOrderResponse;
import com.carigo.pament.dto.VerifyPaymentRequest;
import com.carigo.pament.dto.VerifyPaymentResponse;
import com.carigo.pament.model.Wallet;
import com.carigo.pament.service.PaymentService;
import com.carigo.pament.service.RazorpayService;
import com.carigo.pament.service.WalletService;
import com.razorpay.RazorpayException;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final RazorpayService razorpayService;
    private final PaymentService paymentService;
    private final WalletService walletService;

    // 1) Create Razorpay Order
    @PostMapping("/create-order")
    public ResponseEntity<CreateOrderResponse> createOrder(
            @RequestBody CreateOrderRequest request) throws RazorpayException {

        return ResponseEntity.ok(razorpayService.createOrder(request));
    }

    // 2) Verify Razorpay Payment
    @PostMapping("/verify")
    public ResponseEntity<VerifyPaymentResponse> verifyPayment(
            @RequestBody VerifyPaymentRequest request) {

        return ResponseEntity.ok(paymentService.verifyPayment(request));
    }

    // 3) Owner wallet balance
    @GetMapping("/wallet/{ownerId}")
    public ResponseEntity<Wallet> getWallet(@PathVariable Long ownerId) {
        return ResponseEntity.ok(walletService.getWallet(ownerId));
    }
}
