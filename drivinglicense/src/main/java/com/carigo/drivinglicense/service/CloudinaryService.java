package com.carigo.drivinglicense.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    // UPLOAD --------------------------------------------------------------------
    public String upload(MultipartFile file, String folder) {
        try {
            if (file == null || file.isEmpty())
                return null;

            Map result = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap("folder", folder, "resource_type", "auto"));

            return (String) result.get("secure_url");

        } catch (Exception e) {
            log.error("Cloudinary upload failed → {}", e.getMessage());
            throw new RuntimeException("Image upload failed");
        }
    }

    // DELETE BY URL --------------------------------------------------------------
    public boolean deleteByUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank())
            return false;

        try {
            String publicId = extractPublicId(imageUrl);
            if (publicId == null)
                return false;
            return deleteByPublicId(publicId);

        } catch (Exception e) {
            log.error("Cloudinary delete failed → {}", e.getMessage());
            return false;
        }
    }

    // DELETE BY PUBLIC ID --------------------------------------------------------
    public boolean deleteByPublicId(String publicId) {
        try {
            if (publicId == null || publicId.isBlank())
                return false;

            Map result = cloudinary.uploader().destroy(
                    publicId,
                    ObjectUtils.asMap("resource_type", "image"));

            return "ok".equals(result.get("result"));

        } catch (Exception e) {
            log.error("Failed deleting by publicId → {}", e.getMessage());
            return false;
        }
    }

    // EXTRACT PUBLIC ID ----------------------------------------------------------
    private String extractPublicId(String url) {
        try {
            url = URLDecoder.decode(url, StandardCharsets.UTF_8);

            if (!url.contains("/upload/"))
                return null;

            String[] parts = url.split("/upload/");
            String part = parts[1];

            // Remove version e.g. v1715578123/
            if (part.startsWith("v")) {
                part = part.substring(part.indexOf("/") + 1);
            }

            // Remove extension
            int dot = part.lastIndexOf(".");
            if (dot > 0) {
                part = part.substring(0, dot);
            }

            return part;

        } catch (Exception e) {
            log.error("Failed to extract publicId → {}", e.getMessage());
            return null;
        }
    }
}
