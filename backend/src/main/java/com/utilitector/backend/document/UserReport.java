package com.utilitector.backend.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document("UserReports")
@Data
public class UserReport {
    @Id
    private Long id;

    private String type;

    private String description;
    
    private String location; //TODO
    
    
}
