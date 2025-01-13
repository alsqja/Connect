package com.example.connect.global.enums;

public enum MatchStatus {
    CREATED,
    PENDING,
    ACCEPTED,
    REJECTED;

    public boolean canTransitionTo(MatchStatus newStatus) {
        return switch (this) {
            case PENDING -> newStatus == ACCEPTED || newStatus == REJECTED;
            case CREATED -> newStatus == PENDING || newStatus == REJECTED;
            case ACCEPTED, REJECTED -> false;
        };
    }
}
