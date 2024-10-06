package com.utilitector.backend.service;
 
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.utilitector.backend.document.BotChat;
import com.utilitector.backend.document.BotMessage;
import com.utilitector.backend.document.BotMessageId;
import com.utilitector.backend.mongo.BotChatRepository;
import com.utilitector.backend.mongo.BotMessageRepository;
import com.utilitector.backend.request.BotMessageRequest;
import com.utilitector.backend.response.BotChatResponse;
import com.utilitector.backend.response.BotMessageResponse;

@Service
public class BotService {
    @Autowired private BotChatRepository chatRepo;
    @Autowired private BotMessageRepository msgRepo;
    private ChatClient chatClient;

    SecureRandom rand = new SecureRandom();

    public BotService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public BotChatResponse createChat() {
        BotChat newChat = new BotChat();
        newChat.setId(rand.nextLong());
        while (chatRepo.existsById(newChat.getId())) {
            newChat.setId(rand.nextLong());
        }
        newChat.setNextIndex(1L);
        BotChat savedChat = chatRepo.save(newChat);

        BotMessage firstMessage = new BotMessage();
        firstMessage.setContent("Hello! How can I help you today?");
        firstMessage.setId(new BotMessageId(savedChat.getId(), 0L));
        firstMessage.setMadeByBot(true);
        BotMessage savedFirstMessage = msgRepo.save(firstMessage);

        BotChatResponse response = new BotChatResponse();
        response.setFirstMessage(savedFirstMessage.getContent());
        response.setId(savedFirstMessage.getId().getChatId());

        return response;
    }

    public BotMessageResponse sendMessage(BotMessageRequest messageReq) {
        Optional<BotChat> initChatOpt = chatRepo.findById(messageReq.getId());
        if (initChatOpt.isEmpty())
            return null;
        Long nextIndex = initChatOpt.get().getNextIndex();

        // Save message
        BotMessage userMessage = new BotMessage();
        userMessage.setContent(messageReq.getContent());
        userMessage.setId(new BotMessageId(messageReq.getId(), nextIndex));
        userMessage.setMadeByBot(false);
        BotMessage savedUserMessage = msgRepo.save(userMessage);

        // Increment nextIndex
        Optional<BotChat> chatOpt = chatRepo.findById(savedUserMessage.getId().getChatId());
        if (chatOpt.isEmpty())
            System.err.println("A very weird error happened in BotService");
        BotChat chat = chatOpt.get();
        chat.setNextIndex(chat.getNextIndex() + 1);
        BotChat savedChat = chatRepo.save(chat);

        // Get ChatGPT response based on full message history
        List<BotMessage> messageHistory = msgRepo.findAllByIdChatIdOrderByIdMessageIndex(savedChat.getId());
        List<Message> gptMessages = new ArrayList<>();
        gptMessages.add(new SystemMessage("""
            You are an assistant and you are trying to answer the user's questions."""));
        for (BotMessage bm : messageHistory) {
            if (bm.getMadeByBot()) {
                AssistantMessage assistMsg = new AssistantMessage(bm.getContent());
                gptMessages.add(assistMsg);
            } else {
                UserMessage userMsg = new UserMessage(bm.getContent());
                gptMessages.add(userMsg);
            }
        }
        Prompt prompt = new Prompt(gptMessages);
        String response = chatClient.prompt(prompt).call().chatResponse().getResult().getOutput().getContent();
    
        // Save ChatGPT response
        BotMessage gptMessage = new BotMessage();
        gptMessage.setId(new BotMessageId(savedChat.getId(), nextIndex));
        gptMessage.setContent(response);
        gptMessage.setMadeByBot(true);
        BotMessage savedGptMessage = msgRepo.save(gptMessage);

        // Increment nextIndex
        Optional<BotChat> chatOpt2 = chatRepo.findById(savedGptMessage.getId().getChatId());
        if (chatOpt2.isEmpty())
            System.err.println("A very weird error happened in BotService");
        BotChat chat2 = chatOpt2.get();
        chat2.setNextIndex(chat2.getNextIndex() + 1);
        @SuppressWarnings("unused")
        BotChat savedChat2 = chatRepo.save(chat);

        // Prepare and return message
        BotMessageResponse msgResponse = new BotMessageResponse();
        msgResponse.setContent(savedGptMessage.getContent());
        return msgResponse;
    }
}
