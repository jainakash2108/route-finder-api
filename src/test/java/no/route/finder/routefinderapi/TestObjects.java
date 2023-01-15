package no.route.finder.routefinderapi;

import no.route.finder.routefinderapi.domain.CustomerLocation;

import static java.time.LocalDateTime.now;

public final class TestObjects {

    public static CustomerLocation customerLocation(String sessionId, String vehicleId, String latitude, String longitude) {
        return new CustomerLocation(sessionId, vehicleId, latitude, longitude, now());
    }
}
