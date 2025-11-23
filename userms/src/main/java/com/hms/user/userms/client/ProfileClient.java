package com.hms.user.userms.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.hms.user.userms.config.FeignConfig;
import com.hms.user.userms.dto.ResponseDTO;
import com.hms.user.userms.dto.UserDTO;

@FeignClient(name = "profilems", url = "http://localhost:8081", configuration = FeignConfig.class)
public interface ProfileClient {

    @PostMapping("/renters")
    ResponseDTO saveInProfileRenters(@RequestBody UserDTO sendUser);

    @PostMapping("/car-owners")
    ResponseDTO saveInProfileOwner(@RequestBody UserDTO sendUser);
}
