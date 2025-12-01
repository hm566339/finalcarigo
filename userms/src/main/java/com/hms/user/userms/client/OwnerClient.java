package com.hms.user.userms.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.hms.user.userms.config.FeignConfig;
import com.hms.user.userms.dto.UserResponseDTO;

@FeignClient(name = "ownerClient", url = "http://localhost:8081", configuration = FeignConfig.class)
public interface OwnerClient {

    @DeleteMapping("/car-owners/{id}")
    void deleteOwner(@PathVariable Long id);

    @PostMapping("/car-owners")
    String createOwnerProfile(@RequestBody UserResponseDTO User);
}
