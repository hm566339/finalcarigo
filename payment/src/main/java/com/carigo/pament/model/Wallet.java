package com.carigo.pament.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Wallet {

    @Id
    private Long ownerId; // Same as userId (Owner)

    private double balance;

    private double totalEarnings;

    private LocalDateTime updatedAt = LocalDateTime.now();
}
