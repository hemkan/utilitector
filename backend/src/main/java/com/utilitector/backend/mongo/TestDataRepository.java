package com.utilitector.backend.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.utilitector.backend.document.TestData;

public interface TestDataRepository extends MongoRepository<TestData, Long> {
    
}
