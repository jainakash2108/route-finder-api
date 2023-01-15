package no.route.finder.routefinderapi.domain;

import com.opencsv.bean.CsvBindByName;

public class Coordinates {
    @CsvBindByName(column = "latitude", required = true)
    private String latitude;
    @CsvBindByName(column = "longitude", required = true)
    private String longitude;

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}