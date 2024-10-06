package com.utilitector.backend.response;

import com.utilitector.backend.data.LatitudeLongitude;

import lombok.Data;

@Data
public class NearReportResponse {
    private String type;

    private LatitudeLongitude location;
}
