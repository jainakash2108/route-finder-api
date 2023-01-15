package no.route.finder.routefinderapi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spider-api")
public record SpiderConfig(String url) {
}
