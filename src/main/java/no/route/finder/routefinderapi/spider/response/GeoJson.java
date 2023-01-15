package no.route.finder.routefinderapi.spider.response;

import java.util.List;

public record GeoJson(GeoJsonType type, List<Double> bbox) {
}
