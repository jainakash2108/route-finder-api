package no.route.finder.routefinderapi;

import no.route.finder.routefinderapi.config.SpiderConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableConfigurationProperties({SpiderConfig.class})
@ConfigurationPropertiesScan
public class RouteFinderApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(RouteFinderApiApplication.class, args);
    }

}
