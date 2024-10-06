package com.utilitector.backend.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.utilitector.backend.document.TestData;

@Repository
public interface TestDataRepository extends MongoRepository<TestData, Long> {
    
}
