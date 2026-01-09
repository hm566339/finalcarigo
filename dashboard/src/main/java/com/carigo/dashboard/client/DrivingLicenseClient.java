package com.carigo.dashboard.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.carigo.dashboard.entity.DrivingLicenseDto;

@FeignClient(name = "dl-service", url = "http://localhost:8082")
public interface DrivingLicenseClient {

    @GetMapping("/dl/user/{id}")
    DrivingLicenseDto getByUserId(@PathVariable Long id);

    @GetMapping("/admin/dl/expiring")
    List<DrivingLicenseDto> expiring(@RequestParam int days);
}
