package com.utilitector.backend.request;

import com.utilitector.backend.data.LatitudeLongitude;
import lombok.Data;

@Data
public class ReportRequest {
    private String type;

    private String description;

    private LatitudeLongitude location;
}
