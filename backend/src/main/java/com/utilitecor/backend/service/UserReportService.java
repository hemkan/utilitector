package com.utilitecor.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.utilitecor.backend.entity.UserReport;
import com.utilitecor.backend.repository.UserReportRepository;
import com.utilitecor.backend.response.UserReportResponse;

@Service
public class UserReportService {
    @Autowired private UserReportRepository userReportRepo;

    public List<UserReportResponse> getSampleReports() {
        List<UserReport> reports = userReportRepo.findAll();
        
        List<UserReportResponse> reportResponses = reports.stream().map((UserReport r) -> {
            UserReportResponse res = new UserReportResponse();
            res.setDescription(r.getDescription());
            res.setId(r.getId());
            res.setType(r.getType());
            return res;
        }).toList();
        return reportResponses;
    }
}
