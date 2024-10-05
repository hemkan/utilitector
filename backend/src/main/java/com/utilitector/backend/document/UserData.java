package com.utilitector.backend.document;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document("users")
@Data
public class UserData {
    private String email;

    private String name;
}
