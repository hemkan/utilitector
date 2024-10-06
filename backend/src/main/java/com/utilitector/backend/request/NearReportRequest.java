package com.utilitector.backend.request;

import com.utilitector.backend.data.LatitudeLongitude;

import lombok.Data;

@Data
public class NearReportRequest {
    private LatitudeLongitude location;

    // Miles
    private Double distance;
}
