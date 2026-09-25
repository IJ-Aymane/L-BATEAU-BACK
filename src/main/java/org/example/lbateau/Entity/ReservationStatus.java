package org.example.lbateau.Entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ReservationStatus {
    PENDING,
    CONFIRMED,
    CANCELLED,
    EN_ATTENTE,
    CONFIRMEE,
    ANNULEE;

    @JsonCreator
    public static ReservationStatus from(String value) {
        if (value == null || value.isBlank()) {
            return PENDING;
        }
        return switch (value.trim().toUpperCase()) {
            case "EN_ATTENTE", "EN ATTENTE", "PENDING" -> PENDING;
            case "CONFIRMEE", "CONFIRMÉE", "CONFIRMED" -> CONFIRMED;
            case "ANNULEE", "ANNULÉE", "CANCELLED", "CANCELED" -> CANCELLED;
            default -> ReservationStatus.valueOf(value.trim().toUpperCase());
        };
    }

    public ReservationStatus normalized() {
        return switch (this) {
            case EN_ATTENTE -> PENDING;
            case CONFIRMEE -> CONFIRMED;
            case ANNULEE -> CANCELLED;
            default -> this;
        };
    }

    @JsonValue
    public String jsonValue() {
        return normalized().name();
    }
}
