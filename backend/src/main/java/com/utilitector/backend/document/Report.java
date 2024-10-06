package com.utilitector.backend.document;

import com.utilitector.backend.data.LatitudeLongitude;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

import static com.utilitector.backend.Constants.DB_REPORTS_NAME;

@Document(DB_REPORTS_NAME)
@Data
public class Report {
    private String type;

    private String description;
    
    private LatitudeLongitude location; //TODO
}
