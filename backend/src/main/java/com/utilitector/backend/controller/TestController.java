package com.utilitector.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.utilitector.backend.logic.map.Clustering;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.utilitector.backend.document.TestData;
import com.utilitector.backend.mongo.TestDataRepository;
import com.utilitector.backend.request.TestDataRequest;
import com.utilitector.backend.response.TestDataResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


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

    @GetMapping("/test")
    public ResponseEntity<List<TestDataResponse>> getAllTestData() {
        return ResponseEntity.ok(testRepo.findAll().stream().map((t) -> {
            TestDataResponse res = new TestDataResponse();
            res.setText(t.getText());
            return res;
        }).toList());
    }
	
	@Autowired private Clustering cl;
	
	@GetMapping("/cluster")
	public ResponseEntity<TestDataResponse> posss() {
		cl.doSparkThing();
		return null;
	}
}
