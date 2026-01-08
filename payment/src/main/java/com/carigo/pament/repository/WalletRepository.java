package com.carigo.pament.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carigo.pament.model.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

}
