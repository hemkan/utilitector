package com.utilitector.backend.document;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document("testdata")
@Data
public class TestData {
    private String text;
}
