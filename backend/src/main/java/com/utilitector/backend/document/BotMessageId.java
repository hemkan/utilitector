package com.utilitector.backend.document;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BotMessageId {
    private Long chatId;

    private Long messageIndex;
}
