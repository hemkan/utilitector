package com.utilitector.backend.mongo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.utilitector.backend.document.BotMessage;
import com.utilitector.backend.document.BotMessageId;

@Repository
public interface BotMessageRepository extends MongoRepository<BotMessage, BotMessageId> {
    public List<BotMessage> findAllByIdChatIdOrderByIdMessageIndex(Long chatId);
}
