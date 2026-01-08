package com.carigo.pament.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class HmacSHA256Util {

    private static final String HMAC_SHA256 = "HmacSHA256";

    public static String calculateHMAC(String data, String secret) throws Exception {

        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), HMAC_SHA256);
        Mac mac = Mac.getInstance(HMAC_SHA256);
        mac.init(secretKey);

        byte[] rawHmac = mac.doFinal(data.getBytes());

        // Razorpay expects hex string (NOT base64)
        return bytesToHex(rawHmac);
    }

    // Convert byte array → Hex string
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hash = new StringBuilder();
        for (byte b : bytes) {
            hash.append(String.format("%02x", b));
        }
        return hash.toString();
    }
}
