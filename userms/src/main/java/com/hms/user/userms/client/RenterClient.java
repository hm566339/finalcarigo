package com.hms.user.userms.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.hms.user.userms.config.FeignConfig;
import com.hms.user.userms.dto.UpdateUser;
import com.hms.user.userms.dto.UserResponseDTO;

@FeignClient(name = "renterClient", url = "http://localhost:8081", configuration = FeignConfig.class)
public interface RenterClient {

    @DeleteMapping("/renters/{id}")
    void deleteRenter(@PathVariable Long id);

    @PostMapping("/renters")
    void createRenterProfile(@RequestBody UserResponseDTO responseDTO);

    @PutMapping("/renters/update/email-name/{id}")
    String updateUser(@PathVariable Long id, @RequestBody UpdateUser user);
}
