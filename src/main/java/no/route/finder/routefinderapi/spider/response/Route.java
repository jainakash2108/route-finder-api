package no.route.finder.routefinderapi.spider.response;

import java.util.List;

public record Route(String vehicleId, List<Visit> visits, List<TravelLegData> travelData, List<GeoJson> geometries) {
}
