package com.carigo.booking.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.carigo.booking.config.FeignConfig;
import com.carigo.booking.dto.ReviewRequestDto;

@FeignClient(name = "review", url = "http://localhost:8087", // apne review-ms ka port
        configuration = FeignConfig.class)
public interface ReviewClient {

    @PostMapping("/reviews")
    void submitReview(@RequestBody ReviewRequestDto dto);
}
