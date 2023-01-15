package no.route.finder.routefinderapi.domain;

import java.util.List;

public record Session(String sessionId, List<Location> locations) {
}