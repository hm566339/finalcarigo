package com.carigo.vehicle.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.carigo.vehicle.model.Promo;

@Service
public interface PromoService {

    Promo createPromo(Promo promo);

    List<Promo> getAllPromos();

    Promo updatePromo(Long id, Promo promo);

    void deletePromo(Long id);
}
