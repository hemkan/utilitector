package com.utilitector.backend.response;

import lombok.Data;

@Data
public class LocationIncidentResponse {
    private String location;

    private String type;

    private Long total;
}
