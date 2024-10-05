package com.utilitector.backend.service;
 
import org.springframework.stereotype.Service;

import com.utilitector.backend.request.BotMessageRequest;
import com.utilitector.backend.response.BotChatResponse;
import com.utilitector.backend.response.BotMessageResponse;

@Service
public class BotService {
    public BotChatResponse createChat() {
        BotChatResponse response = new BotChatResponse();
        response.setId(1L);
        return response;
    }

    public BotMessageResponse sendMessage(BotMessageRequest messageReq) {
        BotMessageResponse messageResponse = new BotMessageResponse();
        messageResponse.setContent(messageReq.getContent() + " is what you sent");
        return messageResponse;
    }
}
