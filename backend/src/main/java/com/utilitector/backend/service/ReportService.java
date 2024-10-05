package com.utilitector.backend.service;

import java.util.List;

import com.utilitector.backend.document.Report;
import com.utilitector.backend.mongo.ReportRepository;
import com.utilitector.backend.request.ReportRequest;
import com.utilitector.backend.response.ReportResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportService {
    @Autowired private ReportRepository reportRepo;

    public List<Report> getSampleReports() {
        List<Report> reports = reportRepo.findAll();
        return reports;
    }

    public ReportResponse submitReport(ReportRequest reportReq) {
        Report report = new Report();
        report.setDescription(reportReq.getDescription());
        report.setLocation(reportReq.getLocation());
        report.setType(reportReq.getType());
        Report savedReport = reportRepo.save(report);

        ReportResponse reportResponse = new ReportResponse();
        if (savedReport != null)
            reportResponse.setSuccessful(true);
        return reportResponse;
    }
}
