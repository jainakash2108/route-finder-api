package no.route.finder.routefinderapi.domain;

import java.time.LocalDateTime;

public record CustomerLocation(String sessionId, String vehicleId, String latitude, String longitude,
                               LocalDateTime createdAt) {
}
