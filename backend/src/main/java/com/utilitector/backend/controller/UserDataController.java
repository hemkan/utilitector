package com.utilitector.backend.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.utilitector.backend.document.AreaMonitor;
import com.utilitector.backend.response.UserDataResponse;


@RestController
@RequestMapping("/user")
public class UserDataController {
    
    @GetMapping("/sampleuser")
    public ResponseEntity<List<UserDataResponse>> getSampleUsers() {
        ArrayList<UserDataResponse> users = new ArrayList<>();
        UserDataResponse userDataResponse = new UserDataResponse();

        ArrayList<AreaMonitor> monitoring = new ArrayList<>();
        
        AreaMonitor monitor = new AreaMonitor();
        ArrayList<String> eventTypes = new ArrayList<>();
        
        eventTypes.add("outage");
        eventTypes.add("flood");
        
        monitor.setEventTypes(eventTypes);
        monitoring.add(monitor);

        userDataResponse.setEmail("test@example.com");
        userDataResponse.setName("John Doe");
        userDataResponse.setMonitoring(monitoring);

        users.add(userDataResponse);

        return ResponseEntity.ok(users);
    }
    
}
