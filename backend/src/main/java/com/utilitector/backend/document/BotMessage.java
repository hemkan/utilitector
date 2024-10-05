package com.utilitector.backend.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document("botmessages")
@Data
public class BotMessage {
    @Id
    private BotMessageId id;

    private String content;
}
