package com.utilitector.backend.request;

import lombok.Data;

@Data
public class ReportRequest {
    private String type;

    private String description;

    private String location;
}
