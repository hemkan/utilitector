package com.utilitector.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.utilitector.backend.request.LocationIncidentRequest;
import com.utilitector.backend.response.LocationIncidentResponse;
import com.utilitector.backend.service.ReportService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/location")
public class LocationController {

    @Autowired private ReportService reportService;

    @GetMapping("/incidents")
    public ResponseEntity<List<LocationIncidentResponse>> getIncidentsByLocation(@RequestParam LocationIncidentRequest incidentReq) {
        return ResponseEntity.ok(reportService.getIncidentsByLocation(incidentReq));
    }
    
}
