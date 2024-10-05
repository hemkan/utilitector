package com.utilitector.backend.request;

import lombok.Data;

@Data
public class UserReportRequest {
    private String type;

    private String description;
}
