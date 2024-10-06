package com.utilitector.backend.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document("botchats")
@Data
public class BotChat {
    @Id
    private Long id;

    private Long nextIndex;
}
