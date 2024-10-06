package com.utilitector.backend.document;

import com.utilitector.backend.data.CityListing;
import com.utilitector.backend.data.Incident;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

import java.util.Set;

@Document("users")
@Data
public class UserData {
    private ObjectId _id;
    
    private String email;

    private String name;
    
    @DBRef
    private Set<CityListing> subscribed_cities;
    
    @DBRef
    private Set<Incident> subscribedIncidents;
}
