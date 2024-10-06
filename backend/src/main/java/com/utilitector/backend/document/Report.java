package com.utilitector.backend.document;

import com.utilitector.backend.data.GeolocationCoordinates;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document("reports")
@Data
public class Report {
    private String type;

    private String description;
    
    private GeolocationCoordinates location; //TODO
}
