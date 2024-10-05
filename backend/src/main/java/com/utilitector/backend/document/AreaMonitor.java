package com.utilitector.backend.document;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document("areamonitors")
@Data
public class AreaMonitor {
    @Id
    private Long credentialId;

    private String location;

    // If null or empty, monitor all types
    private List<String> eventTypes;
}
