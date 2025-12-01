package com.carigo.vehicle.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
@Slf4j
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    // -----------------------------------------------------------
    // UPLOAD FILE
    // -----------------------------------------------------------
    public String uploadFile(MultipartFile file, String folder) {
        try {
            if (file == null || file.isEmpty()) {
                return null;
            }

            Map result = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", folder,
                            "resource_type", "auto"));

            return (String) result.get("secure_url");

        } catch (Exception e) {
            log.error("Cloudinary upload failed: {}", e.getMessage());
            throw new RuntimeException("Image upload failed");
        }
    }

    // -----------------------------------------------------------
    // DELETE FILE by public_id
    // -----------------------------------------------------------
    public boolean deleteByPublicId(String publicId) {
        try {
            if (publicId == null || publicId.isBlank())
                return false;

            Map result = cloudinary.uploader().destroy(
                    publicId,
                    ObjectUtils.asMap("resource_type", "image"));

            return "ok".equals(result.get("result"));

        } catch (Exception e) {
            log.error("Cloudinary delete failed: {}", e.getMessage());
            return false;
        }
    }

    // -----------------------------------------------------------
    // DELETE using URL (Important for your system)
    // -----------------------------------------------------------
    public boolean deleteFile(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank())
            return false;

        try {
            String publicId = extractPublicId(imageUrl);

            if (publicId == null) {
                log.error("Could not extract public_id from URL: {}", imageUrl);
                return false;
            }

            return deleteByPublicId(publicId);

        } catch (Exception e) {
            log.error("Failed to delete file from Cloudinary: {}", e.getMessage());
            return false;
        }
    }

    // -----------------------------------------------------------
    // Convert secure_url → public_id
    // -----------------------------------------------------------
    private String extractPublicId(String url) {

        try {
            // Remove URL encoding
            url = URLDecoder.decode(url, StandardCharsets.UTF_8);

            // Example:
            // https://res.cloudinary.com/demo/image/upload/v1715578123/vehicles/123/rc/abcd123xyz.png
            //
            // public_id = vehicles/123/rc/abcd123xyz

            if (!url.contains("/upload/"))
                return null;

            String[] parts = url.split("/upload/");
            String path = parts[1];

            // Remove version prefix v12345/
            if (path.startsWith("v")) {
                path = path.substring(path.indexOf('/') + 1);
            }

            // Remove file extension
            int dotIndex = path.lastIndexOf(".");
            if (dotIndex != -1) {
                path = path.substring(0, dotIndex);
            }

            return path;

        } catch (Exception e) {
            log.error("Failed to extract public_id: {}", e.getMessage());
            return null;
        }
    }
}
