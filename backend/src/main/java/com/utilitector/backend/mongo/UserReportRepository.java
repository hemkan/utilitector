package com.utilitector.backend.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.utilitector.backend.document.UserReport;

@Repository
public interface UserReportRepository extends MongoRepository<UserReport, Long> {
    int countAllByDescription(String description);
}
