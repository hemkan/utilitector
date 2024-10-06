package com.utilitector.backend.request;

import com.utilitector.backend.data.GeolocationCoordinates;
import lombok.Data;

@Data
public class ReportRequest {
    private String type;

    private String description;

    private GeolocationCoordinates location;
}
