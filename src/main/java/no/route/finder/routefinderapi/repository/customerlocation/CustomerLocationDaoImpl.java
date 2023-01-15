package no.route.finder.routefinderapi.repository.customerlocation;

import no.route.finder.routefinderapi.domain.CustomerLocation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class CustomerLocationDaoImpl implements CustomerLocationDao {

    private final JdbcTemplate jdbcTemplate;

    public CustomerLocationDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveAll(List<CustomerLocation> locations) {
        jdbcTemplate.batchUpdate("INSERT INTO customer_location (session_id, vehicle_id, latitude, longitude, created_at) VALUES (?, ?, ?, ?, ?)",
                locations,
                100,
                (PreparedStatement ps, CustomerLocation location) -> {
                    ps.setString(1, location.sessionId());
                    ps.setString(2, location.vehicleId());
                    ps.setString(3, location.latitude());
                    ps.setString(4, location.longitude());
                    ps.setObject(5, location.createdAt());
                });
    }

    @Override
    public List<CustomerLocation> selectBySessionId(String sessionId) {
        String query = "SELECT * FROM customer_location WHERE session_id = ?";
        return jdbcTemplate.query(query, rowMapper, sessionId);
    }

    @Override
    public List<String> selectAllSessionId() {
        String query = "SELECT distinct session_id FROM customer_location";
        return jdbcTemplate.queryForList(query, String.class);
    }

    @Override
    public void deleteBySessionId(String sessionId) {
        String query = "DELETE FROM customer_location WHERE session_id = ?";
        jdbcTemplate.update(query, sessionId);
    }

    @Override
    public void deleteAll() {
        String query = "DELETE FROM customer_location";
        jdbcTemplate.update(query);
    }

    private final RowMapper<CustomerLocation> rowMapper = (rs, i) -> new CustomerLocation(
            rs.getString("session_id"),
            rs.getString("vehicle_id"),
            rs.getString("latitude"),
            rs.getString("longitude"),
            rs.getObject("created_at", LocalDateTime.class)
    );
}
