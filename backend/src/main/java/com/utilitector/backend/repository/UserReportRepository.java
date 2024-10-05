package com.utilitector.backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.utilitector.backend.entity.UserReport;

public interface UserReportRepository extends MongoRepository<UserReport, Long> {
    int countAllByDescription(String description);
}
