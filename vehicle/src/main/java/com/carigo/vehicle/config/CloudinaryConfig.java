package com.carigo.vehicle.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    private static final Logger log = LoggerFactory.getLogger(CloudinaryConfig.class);

    @Value("${cloudinary.cloud_name}")
    private String cloudName;

    @Value("${cloudinary.api_key}")
    private String apiKey;

    @Value("${cloudinary.api_secret}")
    private String apiSecret;

    @Bean
    public Cloudinary cloudinary() {

        // ⭐ Validation for debugging
        if (cloudName == null || cloudName.isBlank()) {
            log.error("❌ Cloudinary cloud_name is missing!");
        }
        if (apiKey == null || apiKey.isBlank()) {
            log.error("❌ Cloudinary api_key is missing!");
        }
        if (apiSecret == null || apiSecret.isBlank()) {
            log.error("❌ Cloudinary api_secret is missing!");
        }

        log.info("✔ Cloudinary initialized with cloud_name: {}", cloudName);

        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret,
                "secure", true));
    }
}
