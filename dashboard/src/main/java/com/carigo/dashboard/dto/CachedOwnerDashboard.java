package com.carigo.dashboard.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CachedOwnerDashboard {

    private OwnerDashboardResponse data;
    private long cachedAt;
}
