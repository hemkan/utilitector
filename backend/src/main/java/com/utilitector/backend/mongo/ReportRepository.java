package com.utilitector.backend.mongo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.utilitector.backend.document.Report;

@Repository
public interface ReportRepository extends MongoRepository<Report, Long> {
    int countAllByDescription(String description);

    List<Report> findAllByLocation(String location);
}
