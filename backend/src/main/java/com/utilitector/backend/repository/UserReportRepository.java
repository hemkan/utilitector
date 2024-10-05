package com.utilitector.backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.utilitector.backend.entity.UserReport;
import org.springframework.stereotype.Repository;

public interface UserReportRepository extends MongoRepository<UserReport, Long> {
    int countAllByDescription(String description);
}
