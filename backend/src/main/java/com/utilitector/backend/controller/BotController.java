package com.utilitector.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.utilitector.backend.request.BotMessageRequest;
import com.utilitector.backend.response.BotChatResponse;
import com.utilitector.backend.response.BotMessageResponse;
import com.utilitector.backend.service.BotService;


@RestController
@RequestMapping("/bot")
public class BotController {
    @Autowired private BotService botService;

    @PostMapping("/new-chat")
    public ResponseEntity<BotChatResponse> createChat() {
        BotChatResponse response = botService.createChat();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/message")
    public ResponseEntity<BotMessageResponse> sendMessage(@RequestBody BotMessageRequest messageReq) {
        BotMessageResponse messageResponse = botService.sendMessage(messageReq);
        return ResponseEntity.ok(messageResponse);
    }

}
