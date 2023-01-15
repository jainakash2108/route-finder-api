package no.route.finder.routefinderapi.service.csv;

import no.route.finder.routefinderapi.domain.Coordinates;
import no.route.finder.routefinderapi.domain.CustomerLocation;
import no.route.finder.routefinderapi.repository.customerlocation.CustomerLocationDao;
import no.route.finder.routefinderapi.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;

@Service
public class CsvFileService implements FileService {
    private final CustomerLocationDao customerLocationDao;
    private final CsvFileProcessor csvFileProcessor;

    public CsvFileService(CustomerLocationDao customerLocationDao, CsvFileProcessor csvFileProcessor) {
        this.customerLocationDao = customerLocationDao;
        this.csvFileProcessor = csvFileProcessor;
    }

    @Override
    @Transactional
    public void process(MultipartFile file) {
        List<Coordinates> coordinates = csvFileProcessor.convertToBean(file);
        String sessionId = UUID.randomUUID().toString();
        // Store customer locations and assign unique id called session id
        List<CustomerLocation> locations = mapToCustomerLocation(coordinates, sessionId);
        customerLocationDao.saveAll(locations);
    }

    private List<CustomerLocation> mapToCustomerLocation(List<Coordinates> coordinates, String sessionId) {
        LocalDateTime createdAt = now();
        return coordinates.stream().map(coordinate -> new CustomerLocation(sessionId, "car_" + coordinates.indexOf(coordinate), coordinate.getLatitude(), coordinate.getLongitude(), createdAt)).collect(Collectors.toList());
    }

}
