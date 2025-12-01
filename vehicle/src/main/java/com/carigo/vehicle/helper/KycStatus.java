package com.carigo.vehicle.helper;

public enum KycStatus {

    PENDING("Pending Verification"),
    VERIFIED("Verified Successfully"),
    FAILED("Verification Failed"),
    MANUAL_CHECK("Manual Review Required");

    private final String label;

    KycStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    /**
     * Safe parser – converts string to enum without throwing error.
     * Returns null if invalid string.
     */
    public static KycStatus fromString(String value) {
        if (value == null)
            return null;

        for (KycStatus status : KycStatus.values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        return null; // ⚠ invalid string
    }
}
