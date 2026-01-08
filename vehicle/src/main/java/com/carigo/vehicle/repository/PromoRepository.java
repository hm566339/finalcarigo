package com.carigo.vehicle.repository;

import com.carigo.vehicle.model.Promo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PromoRepository extends JpaRepository<Promo, Long> {

    Optional<Promo> findByCodeIgnoreCaseAndActiveIsTrue(String code);
}
