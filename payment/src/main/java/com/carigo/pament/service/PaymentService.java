package com.carigo.pament.service;

import org.springframework.stereotype.Service;

import com.carigo.pament.dto.VerifyPaymentRequest;
import com.carigo.pament.dto.VerifyPaymentResponse;
import com.carigo.pament.model.PaymentTransaction;
import com.carigo.pament.repository.PaymentRepository;
import com.carigo.pament.util.HmacSHA256Util;
import com.carigo.pament.mapper.PaymentMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final WalletService walletService;
    private final PaymentMapper paymentMapper;

    // ----------------------------- VERIFY PAYMENT -----------------------------
    public VerifyPaymentResponse verifyPayment(VerifyPaymentRequest request) {

        String payload = request.getOrderId() + "|" + request.getPaymentId();

        String actualSignature;
        try {
            actualSignature = HmacSHA256Util.calculateHMAC(payload, request.getSecret());
        } catch (Exception e) {
            throw new RuntimeException("Error generating payment signature");
        }

        if (!actualSignature.equals(request.getSignature())) {
            throw new RuntimeException("Payment signature mismatch");
        }

        PaymentTransaction tx = paymentMapper.toEntityFromVerify(request);

        paymentRepository.save(tx);

        walletService.credit(request.getOwnerId(), request.getAmount());

        return paymentMapper.toVerifyResponse(tx);
    }
}
