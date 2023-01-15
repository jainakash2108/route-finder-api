package no.route.finder.routefinderapi.spider.response;

public record Visit(VisitType type, String orderId, String startTime, String duration) {
}
