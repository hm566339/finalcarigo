package com.carigo.vehicle.service.imp;

import com.carigo.vehicle.dto.PriceQuoteDto;
import com.carigo.vehicle.dto.PriceQuoteRequest;
import com.carigo.vehicle.exception.ResourceNotFoundException;
import com.carigo.vehicle.helper.BillingMode;
import com.carigo.vehicle.model.Promo;
import com.carigo.vehicle.model.VehicleAvailabilitySlot;
import com.carigo.vehicle.model.VehicleEntity;
import com.carigo.vehicle.repository.PromoRepository;
import com.carigo.vehicle.repository.VehicleAvailabilityRepository;
import com.carigo.vehicle.repository.VehicleRepository;
import com.carigo.vehicle.service.PricingService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PricingServiceImpl implements PricingService {

    private final VehicleRepository vehicleRepository;
    private final VehicleAvailabilityRepository availabilityRepository;
    private final PromoRepository promoRepository;

    @Override
    public PriceQuoteDto calculateQuote(String vehicleId, PriceQuoteRequest request) {
        VehicleEntity vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        LocalDateTime from = request.getStartTime();
        LocalDateTime to = request.getEndTime();

        if (from == null || to == null || !to.isAfter(from)) {
            throw new IllegalArgumentException("Invalid time range");
        }

        // 1) Check availability
        List<VehicleAvailabilitySlot> slots = availabilityRepository
                .findByVehicleIdAndEndTimeGreaterThanEqualAndStartTimeLessThanEqual(
                        vehicleId, from, to);

        boolean anyAvailable = slots.stream().anyMatch(VehicleAvailabilitySlot::isAvailable);
        if (!anyAvailable) {
            throw new IllegalStateException("Vehicle not available in requested time range");
        }

        // 2) Base price calculation
        BigDecimal baseAmount = calculateBaseAmount(vehicle, from, to);

        // 3) Apply promo
        PriceQuoteDto quote = new PriceQuoteDto();
        quote.setBaseAmount(baseAmount);
        quote.setFinalAmount(baseAmount);

        if (request.getPromoCode() != null && !request.getPromoCode().isBlank()) {
            applyPromo(quote, request.getPromoCode(), baseAmount);
        }

        return quote;
    }

    private BigDecimal calculateBaseAmount(VehicleEntity vehicle, LocalDateTime from, LocalDateTime to) {
        Duration duration = Duration.between(from, to);
        long totalMinutes = duration.toMinutes();
        BigDecimal base = BigDecimal.ZERO;

        BillingMode mode = vehicle.getBillingMode() != null ? vehicle.getBillingMode() : BillingMode.PER_DAY;

        switch (mode) {
            case PER_HOUR -> {
                BigDecimal hours = BigDecimal.valueOf(Math.ceil(totalMinutes / 60.0));
                base = vehicle.getRatePerHour().multiply(hours);
            }
            case PER_DAY -> {
                BigDecimal days = BigDecimal.valueOf(Math.ceil(totalMinutes / (60.0 * 24)));
                base = vehicle.getRatePerDay().multiply(days);
            }
            case BOTH -> {
                // simple strategy: use per-hour if less than 8h, else per-day
                long hoursLong = (long) Math.ceil(totalMinutes / 60.0);
                BigDecimal perHourCost = vehicle.getRatePerHour()
                        .multiply(BigDecimal.valueOf(hoursLong));

                long daysLong = (long) Math.ceil(totalMinutes / (60.0 * 24));
                BigDecimal perDayCost = vehicle.getRatePerDay()
                        .multiply(BigDecimal.valueOf(daysLong));

                base = perHourCost.min(perDayCost);
            }
        }

        return base;
    }

    private void applyPromo(PriceQuoteDto quote, String promoCode, BigDecimal baseAmount) {
        Promo promo = promoRepository.findByCodeIgnoreCaseAndActiveIsTrue(promoCode)
                .orElseThrow(() -> new ResourceNotFoundException("Promo code not found or inactive"));

        LocalDateTime now = LocalDateTime.now();
        if (promo.getValidFrom() != null && now.isBefore(promo.getValidFrom())
                || promo.getValidTo() != null && now.isAfter(promo.getValidTo())) {
            throw new IllegalStateException("Promo code is not valid at this time");
        }

        if (promo.getMinBookingAmount() != null
                && baseAmount.compareTo(promo.getMinBookingAmount()) < 0) {
            throw new IllegalStateException("Booking amount does not meet minimum for this promo");
        }

        BigDecimal discount = BigDecimal.ZERO;

        if (promo.getDiscountPercent() != null) {
            discount = discount.add(
                    baseAmount.multiply(promo.getDiscountPercent())
                            .divide(BigDecimal.valueOf(100)));
        }

        if (promo.getDiscountAmount() != null) {
            discount = discount.add(promo.getDiscountAmount());
        }

        if (promo.getMaxDiscountAmount() != null
                && discount.compareTo(promo.getMaxDiscountAmount()) > 0) {
            discount = promo.getMaxDiscountAmount();
        }

        BigDecimal finalAmount = baseAmount.subtract(discount);
        if (finalAmount.compareTo(BigDecimal.ZERO) < 0) {
            finalAmount = BigDecimal.ZERO;
        }

        quote.setPromoCode(promoCode);
        quote.setDiscount(discount);
        quote.setFinalAmount(finalAmount);
        quote.setPromoMessage("Promo applied successfully");
    }
}
