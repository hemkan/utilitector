package com.utilitector.backend.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.utilitector.backend.document.BotChat;

@Repository
public interface BotChatRepository extends MongoRepository<BotChat, Long> {
}
