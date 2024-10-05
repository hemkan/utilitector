package com.utilitector.backend.document;

import lombok.Data;

@Data
public class BotMessageId {
    private Long chatId;

    private Long messageIndex;
}
