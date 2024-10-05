package com.utilitector.backend.request;

import lombok.Data;

@Data
public class BotMessageRequest {
    private Long id;

    private String content;
}
