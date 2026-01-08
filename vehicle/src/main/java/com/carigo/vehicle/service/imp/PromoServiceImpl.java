package com.carigo.vehicle.service.imp;

import com.carigo.vehicle.model.Promo;
import com.carigo.vehicle.repository.PromoRepository;
import com.carigo.vehicle.service.PromoService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PromoServiceImpl implements PromoService {

    private final PromoRepository promoRepository;

    @Override
    public Promo createPromo(Promo promo) {

        // ✅ unique promo code check
        promoRepository.findByCodeIgnoreCaseAndActiveIsTrue(promo.getCode())
                .ifPresent(p -> {
                    throw new RuntimeException("Promo code already exists");
                });

        return promoRepository.save(promo);
    }

    @Override
    public List<Promo> getAllPromos() {
        return promoRepository.findAll();
    }

    @Override
    public Promo updatePromo(Long id, Promo req) {

        Promo promo = promoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promo not found"));

        promo.setDescription(req.getDescription());
        promo.setDiscountPercent(req.getDiscountPercent());
        promo.setDiscountAmount(req.getDiscountAmount());
        promo.setMaxDiscountAmount(req.getMaxDiscountAmount());
        promo.setValidFrom(req.getValidFrom());
        promo.setValidTo(req.getValidTo());
        promo.setMinBookingAmount(req.getMinBookingAmount());
        promo.setActive(req.isActive());

        return promoRepository.save(promo);
    }

    @Override
    public void deletePromo(Long id) {
        promoRepository.deleteById(id);
    }
}
