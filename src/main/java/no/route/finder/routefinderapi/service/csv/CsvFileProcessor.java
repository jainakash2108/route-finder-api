package no.route.finder.routefinderapi.service.csv;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import no.route.finder.routefinderapi.domain.Coordinates;
import no.route.finder.routefinderapi.exception.CsvFileNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Component
public class CsvFileProcessor {

    public List<Coordinates> convertToBean(MultipartFile file) {
        BufferedReader fileReader = null;
        try {
            fileReader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            CsvToBean<Coordinates> csvToBean = createCSVToBean(fileReader);
            List<Coordinates> locations = csvToBean.parse();
            if (locations.isEmpty()) {
                throw new CsvFileNotFoundException("File is empty");
            }
            return locations;
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        } finally {
            closeFileReader(fileReader);
        }
    }

    private void closeFileReader(BufferedReader fileReader) {
        if (fileReader != null) {
            try {
                fileReader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private CsvToBean<Coordinates> createCSVToBean(BufferedReader fileReader) {
        return new CsvToBeanBuilder<Coordinates>(fileReader).withType(Coordinates.class).withIgnoreLeadingWhiteSpace(true).build();
    }

}
