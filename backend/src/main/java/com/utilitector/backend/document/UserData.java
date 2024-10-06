package com.utilitector.backend.document;

import com.utilitector.backend.data.CityListing;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

import java.util.Set;

@Document("users")
@Data
public class UserData {
    private String email;

    private String name;
    
    private Set<CityListing> subscribed_cities;
}
