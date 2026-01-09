package com.carigo.dashboard.service;

import com.carigo.dashboard.dto.RenterDashboardDTO;

public interface RenterDashboardService {

    RenterDashboardDTO getRenterDashboard(Long renterId);
}
