package no.route.finder.routefinderapi.config;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;

import static java.time.Duration.ofSeconds;

public class PostgresContainer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private PostgreSQLContainer container = (PostgreSQLContainer) new PostgreSQLContainer("postgres:14.3-alpine")
            .withDatabaseName("route-finder-api")
            .withUsername("route-finder-api")
            .withPassword("route-finder-api")
            .withStartupTimeout(ofSeconds(30));

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        if (!container.isRunning()) {
            container.start();
        }
        TestPropertyValues.of("spring.datasource.url=" + container.getJdbcUrl())
                .applyTo(applicationContext.getEnvironment());
    }
}
