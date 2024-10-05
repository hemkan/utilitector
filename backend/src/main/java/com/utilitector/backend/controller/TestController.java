package com.utilitector.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.utilitector.backend.document.TestData;
import com.utilitector.backend.mongo.TestDataRepository;
import com.utilitector.backend.request.TestDataRequest;
import com.utilitector.backend.response.TestDataResponse;


@RestController
public class TestController {
    @Autowired private TestDataRepository testRepo;

    @PostMapping("/test")
    public ResponseEntity<TestDataResponse> postRandomData(@RequestBody TestDataRequest testData) {
        TestData data = new TestData();
        data.setText(testData.getText());
        TestData savedData = testRepo.save(data);
        TestDataResponse response = new TestDataResponse();
        response.setText(savedData.getText());
        return ResponseEntity.ok(response);
    }
    
}
