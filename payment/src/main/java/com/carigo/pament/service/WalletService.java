package com.carigo.pament.service;

import org.springframework.stereotype.Service;

import com.carigo.pament.model.Wallet;
import com.carigo.pament.repository.WalletRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    public Wallet getWallet(Long ownerId) {
        return walletRepository.findById(ownerId)
                .orElseGet(() -> {
                    Wallet wallet = new Wallet();
                    wallet.setOwnerId(ownerId);
                    wallet.setBalance(0);
                    return walletRepository.save(wallet);
                });
    }

    public void credit(Long ownerId, double amount) {
        Wallet wallet = getWallet(ownerId);
        wallet.setBalance(wallet.getBalance() + amount);
        wallet.setTotalEarnings(wallet.getTotalEarnings() + amount);
        walletRepository.save(wallet);
    }

    public void debit(Long ownerId, double amount) {
        Wallet wallet = getWallet(ownerId);

        if (wallet.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }

        wallet.setBalance(wallet.getBalance() - amount);
        walletRepository.save(wallet);
    }
}
