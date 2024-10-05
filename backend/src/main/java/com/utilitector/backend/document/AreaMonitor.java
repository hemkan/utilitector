package com.utilitector.backend.document;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document("areamonitors")
@Data
public class AreaMonitor {
    private String location;

    // If null or empty, monitor all types
    private List<String> eventTypes;
}
