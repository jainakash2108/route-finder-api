package no.route.finder.routefinderapi.repository.customerlocation;

import no.route.finder.routefinderapi.config.PostgresContainer;
import no.route.finder.routefinderapi.domain.CustomerLocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static no.route.finder.routefinderapi.TestObjects.customerLocation;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.testcontainers.shaded.com.google.common.collect.ImmutableList.of;

@JdbcTest
@ContextConfiguration(initializers = PostgresContainer.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerLocationDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private CustomerLocationDao customerLocationDao;

    @BeforeEach
    public void reset() {
        customerLocationDao = new CustomerLocationDaoImpl(jdbcTemplate);
        customerLocationDao.deleteAll();
    }

    @Test
    public void should_save_location_in_batch() {
        List<CustomerLocation> customerLocations = of(
                customerLocation("session-id-1", "car-1", "56.8312000436932", "23.9368456981301"),
                customerLocation("session-id-1", "car-2", "59.8312000436932", "53.9368456981301"),
                customerLocation("session-id-1", "car-3", "55.8312000436932", "83.9368456981301"),
                customerLocation("session-id-3", "car-4", "56.8312000436932", "33.9368456981301"),
                customerLocation("session-id-4", "car-5", "58.8312000436932", "63.9368456981301"),
                customerLocation("session-id-6", "car-6", "51.8312000436932", "93.9368456981301"),
                customerLocation("session-id-6", "car-7", "56.8312000436932", "63.9368456981301"),
                customerLocation("session-id-6", "car-8", "53.8312000436932", "13.9368456981301"),
                customerLocation("session-id-6", "car-9", "56.8312000436932", "43.9368456981301"));
        customerLocationDao.saveAll(customerLocations);
        assertEquals(4, customerLocationDao.selectAllSessionId().size());
    }

    @Test
    public void should_fetch_location_by_sessionId() {
        List<CustomerLocation> customerLocations = of(
                customerLocation("session-id-1", "car-1", "56.8312000436932", "23.9368456981301"),
                customerLocation("session-id-1", "car-2", "59.8312000436932", "53.9368456981301"),
                customerLocation("session-id-1", "car-3", "55.8312000436932", "83.9368456981301"),
                customerLocation("session-id-3", "car-4", "56.8312000436932", "33.9368456981301"),
                customerLocation("session-id-4", "car-5", "58.8312000436932", "63.9368456981301"),
                customerLocation("session-id-6", "car-6", "51.8312000436932", "93.9368456981301"),
                customerLocation("session-id-6", "car-7", "56.8312000436932", "63.9368456981301"),
                customerLocation("session-id-6", "car-8", "53.8312000436932", "13.9368456981301"),
                customerLocation("session-id-6", "car-9", "56.8312000436932", "43.9368456981301"));
        customerLocationDao.saveAll(customerLocations);
        assertEquals(3, customerLocationDao.selectBySessionId("session-id-1").size());
    }

    @Test
    public void should_delete_location_by_sessionId() {
        List<CustomerLocation> customerLocations = of(
                customerLocation("session-id-1", "car-1", "56.8312000436932", "23.9368456981301"),
                customerLocation("session-id-1", "car-2", "59.8312000436932", "53.9368456981301"),
                customerLocation("session-id-1", "car-3", "55.8312000436932", "83.9368456981301"),
                customerLocation("session-id-3", "car-4", "56.8312000436932", "33.9368456981301"),
                customerLocation("session-id-4", "car-5", "58.8312000436932", "63.9368456981301"),
                customerLocation("session-id-6", "car-6", "51.8312000436932", "93.9368456981301"),
                customerLocation("session-id-6", "car-7", "56.8312000436932", "63.9368456981301"),
                customerLocation("session-id-6", "car-8", "53.8312000436932", "13.9368456981301"),
                customerLocation("session-id-6", "car-9", "56.8312000436932", "43.9368456981301"));
        customerLocationDao.saveAll(customerLocations);
        customerLocationDao.deleteBySessionId("session-id-1");
        assertEquals(0, customerLocationDao.selectBySessionId("session-id-1").size());
    }

}