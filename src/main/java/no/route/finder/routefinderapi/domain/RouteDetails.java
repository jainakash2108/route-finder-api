package no.route.finder.routefinderapi.domain;

import no.route.finder.routefinderapi.spider.response.Route;

import java.util.List;

public record RouteDetails(List<Route> routes, List<UnservicedOrder> unservicedOrders) {
}
