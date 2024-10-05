package com.utilitector.backend.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document("users")
@Data
public class UserData {
    @Id
    private Long credentialId;

    private String email;

    private String name;
}
