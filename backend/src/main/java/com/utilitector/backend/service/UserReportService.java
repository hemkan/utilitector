package com.utilitector.backend.service;

import java.util.List;

import com.utilitector.backend.entity.UserReport;
import com.utilitector.backend.repository.UserReportRepository;
import com.utilitector.backend.response.UserReportResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
