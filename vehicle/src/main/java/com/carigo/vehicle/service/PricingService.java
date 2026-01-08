package com.carigo.vehicle.service;

import com.carigo.vehicle.dto.PriceQuoteDto;
import com.carigo.vehicle.dto.PriceQuoteRequest;

public interface PricingService {
    PriceQuoteDto calculateQuote(String vehicleId, PriceQuoteRequest request);
}
