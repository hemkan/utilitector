package com.utilitector.backend.document;

import com.utilitector.backend.data.CityListing;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

import java.util.Set;

@Document("users")
@Data
public class UserData {
    // TODO make ID object to be foreign key in citylisting
    private ObjectId _id;
    
    private String email;

    private String name;
    
    private Set<CityListing> subscribed_cities;
    
    private Set<Incident> subscribedIncidents;
    
    public record UserId(ObjectId id) {}
}
