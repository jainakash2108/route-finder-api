package no.route.finder.routefinderapi.repository.customerlocation;

import no.route.finder.routefinderapi.domain.CustomerLocation;

import java.util.List;

public interface CustomerLocationDao {

    void saveAll(List<CustomerLocation> locations);

    List<CustomerLocation> selectBySessionId(String sessionId);

    void deleteBySessionId(String sessionId);

    List<String> selectAllSessionId();

    void deleteAll();
}