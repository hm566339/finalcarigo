package com.carigo.pament.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carigo.pament.model.PaymentTransaction;

public interface PaymentRepository extends JpaRepository<PaymentTransaction, Long> {

    Optional<PaymentTransaction> findByOrderId(String orderId);

}
