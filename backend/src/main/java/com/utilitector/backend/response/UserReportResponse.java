package com.utilitector.backend.response;

import lombok.Data;

@Data
public class UserReportResponse {
    private Long id;

    private String type;

    private String description;
}
