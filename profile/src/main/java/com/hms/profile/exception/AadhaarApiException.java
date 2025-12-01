package com.hms.profile.exception;

public class AadhaarApiException extends RuntimeException {
    public AadhaarApiException(String message) {
        super(message);
    }

    public AadhaarApiException(String message, Throwable cause) {
        super(message, cause);
    }
}