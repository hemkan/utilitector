package com.utilitector.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.utilitector.backend.entity.UserReport;

@Repository
public interface UserReportRepository extends JpaRepository<UserReport, Long> {
    
}
