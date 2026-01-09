package com.carigo.dashboard.service;

import com.carigo.dashboard.dto.OwnerDashboardResponse;

public interface OwnerDashboardService {

    /**
     * Get owner dashboard (mobile)
     */
    OwnerDashboardResponse getOwnerDashboard(String authorizationHeader);
}
