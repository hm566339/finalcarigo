package com.carigo.pament.mapper;

import org.springframework.stereotype.Component;

import com.carigo.pament.dto.CreateOrderRequest;
import com.carigo.pament.dto.TransferRequest;
import com.carigo.pament.dto.VerifyPaymentRequest;
import com.carigo.pament.dto.VerifyPaymentResponse;
import com.carigo.pament.model.PaymentTransaction;
import com.carigo.pament.model.PaymentStatus; // ✅ Correct import

@Component
public class PaymentMapper {

    // ----------------------------- CREATE PAYMENT FROM ORDER
    // -----------------------------
    public PaymentTransaction toEntityFromOrder(CreateOrderRequest request, String razorpayOrderId) {
        PaymentTransaction tx = new PaymentTransaction();

        tx.setOrderId(razorpayOrderId);
        tx.setBookingId(request.getBookingId());
        tx.setRenterId(request.getRenterId());
        tx.setOwnerId(request.getOwnerId());
        tx.setAmount(request.getAmount());
        tx.setCurrency(request.getCurrency()); // ✅ ensure currency saved
        tx.setStatus(PaymentStatus.PENDING); // ✅ correct enum

        return tx;
    }

    // ----------------------------- FROM VERIFY PAYMENT REQUEST
    // -----------------------------
    public PaymentTransaction toEntityFromVerify(VerifyPaymentRequest request) {
        PaymentTransaction tx = new PaymentTransaction();

        tx.setOrderId(request.getOrderId());
        tx.setPaymentId(request.getPaymentId());
        tx.setSignature(request.getSignature());

        tx.setRenterId(request.getRenterId());
        tx.setOwnerId(request.getOwnerId());
        tx.setBookingId(request.getBookingId());

        tx.setAmount(request.getAmount());
        tx.setCurrency("INR"); // Razorpay fixed for India

        tx.setStatus(PaymentStatus.SUCCESS);

        return tx;
    }

    // ----------------------------- ENTITY TO RESPONSE DTO
    // -----------------------------
    public VerifyPaymentResponse toVerifyResponse(PaymentTransaction tx) {
        return new VerifyPaymentResponse(
                "PAYMENT_SUCCESS",
                tx.getPaymentId());
    }

    // ----------------------------- TRANSFER REQUEST TO ENTITY
    // -----------------------------
    public PaymentTransaction toEntityFromTransfer(TransferRequest req) {
        PaymentTransaction tx = new PaymentTransaction();

        tx.setOwnerId(req.getOwnerId());
        tx.setAmount(req.getAmount());
        tx.setCurrency("INR");
        tx.setStatus(PaymentStatus.REFUNDED); // money sent to bank

        tx.setPayoutAccount(req.getAccountNumber()); // optional fields
        tx.setIfsc(req.getIfscCode());
        tx.setAccountHolder(req.getAccountHolderName());

        return tx;
    }
}
