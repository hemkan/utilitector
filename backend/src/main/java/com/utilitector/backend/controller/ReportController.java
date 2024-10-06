package com.utilitector.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.utilitector.backend.document.Report;
import com.utilitector.backend.request.ReportRequest;
import com.utilitector.backend.response.ReportResponse;
import com.utilitector.backend.service.ReportService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/report")
public class ReportController {
    @Autowired private ReportService reportService;

    @GetMapping("/test")
    public ResponseEntity<List<Report>> getSampleReports() {
        List<Report> reportResponses = reportService.getSampleReports();
        return ResponseEntity.ok(reportResponses);
    }

    @PostMapping("/submit")
    public ResponseEntity<ReportResponse> submitReport(@RequestBody ReportRequest reportReq) {
        ReportResponse reportResponse = reportService.submitReport(reportReq);
        if (!reportResponse.getSuccessful())
            return ResponseEntity.internalServerError().body(reportResponse);
        return ResponseEntity.ok(reportResponse);
    }
    
}
