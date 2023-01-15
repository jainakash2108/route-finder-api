package no.route.finder.routefinderapi.spider.response;

import java.util.List;

public record VrpSolution(List<Route> routes, List<String> unservicedOrderIds) {
}
