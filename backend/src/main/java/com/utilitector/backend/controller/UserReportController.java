package com.utilitector.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.utilitector.backend.response.UserReportResponse;
import com.utilitector.backend.service.UserReportService;


@RestController
@RequestMapping("/user-report")
public class UserReportController {
    @Autowired private UserReportService userReportService;

    @GetMapping("/test")
    public ResponseEntity<List<UserReportResponse>> getSampleReports() {
        List<UserReportResponse> userReportResponses = userReportService.getSampleReports();
        return ResponseEntity.ok(userReportResponses);
    }
    
}
