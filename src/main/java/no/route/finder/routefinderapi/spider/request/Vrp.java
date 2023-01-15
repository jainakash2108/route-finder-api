package no.route.finder.routefinderapi.spider.request;

import java.util.List;

public record Vrp(Topology topology, List<Vehicle> vehicles, List<Order> orders) {
}

