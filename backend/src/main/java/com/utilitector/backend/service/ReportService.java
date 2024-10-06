package com.utilitector.backend.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

import com.utilitector.backend.document.Report;
import com.utilitector.backend.mongo.ReportRepository;
import com.utilitector.backend.request.LocationIncidentRequest;
import com.utilitector.backend.request.NearReportRequest;
import com.utilitector.backend.request.ReportRequest;
import com.utilitector.backend.response.LocationIncidentResponse;
import com.utilitector.backend.response.NearReportResponse;
import com.utilitector.backend.response.ReportResponse;

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

    public List<LocationIncidentResponse> getIncidentsByLocation(LocationIncidentRequest incidentRequest) {
        List<Report> reports = reportRepo.findAllByLocation(incidentRequest.getLocation());
        List<LocationIncidentResponse> incidentResponses = new ArrayList<>();
        HashMap<String, Long> reportTypeCount = new HashMap<>();

        // Summarize reports
        for (Report r : reports) {
            if (reportTypeCount.containsKey(r.getType())) {
                reportTypeCount.put(r.getType(), 1L);
            } else {
                reportTypeCount.put(r.getType(), reportTypeCount.get(r.getType()) + 1);
            }
        }

        // Create responses
        for (var entry : reportTypeCount.entrySet()) {
            LocationIncidentResponse response = new LocationIncidentResponse();
            response.setLocation(incidentRequest.getLocation());
            response.setType(entry.getKey());
            response.setTotal(entry.getValue());
            incidentResponses.add(response);
        }

        return incidentResponses;
    }

    public List<NearReportResponse> getNearReports(NearReportRequest req) {
        Point point = new Point(req.getLocation().getLongitude(), req.getLocation().getLatitude());
        Distance dist = new Distance(req.getDistance(), Metrics.MILES);
        List<Report> near = reportRepo.findAllByLocationNear(point, dist);
        List<NearReportResponse> nearRes = near.stream().map((r) -> {
            NearReportResponse res = new NearReportResponse();
            res.setLocation(r.getLocation());
            res.setType(r.getType());
            return res;
        }).toList();
        return nearRes;
    }
}
