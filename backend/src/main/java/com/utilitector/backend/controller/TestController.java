package com.utilitector.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.utilitector.backend.document.TestData;
import com.utilitector.backend.logic.map.Clustering;
import com.utilitector.backend.mongo.TestDataRepository;
import com.utilitector.backend.request.TestPAuthRequest;
import com.utilitector.backend.request.TestTAuthRequest;
import com.utilitector.backend.request.TestDataRequest;
import com.utilitector.backend.response.TestDataResponse;
import com.utilitector.backend.service.AuthService;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired private TestDataRepository testRepo;
    @Autowired private AuthService authService;

    @PostMapping("")
    public ResponseEntity<TestDataResponse> postRandomData(@RequestBody TestDataRequest testData) {
        TestData data = new TestData();
        data.setText(testData.getText());
        TestData savedData = testRepo.save(data);
        TestDataResponse response = new TestDataResponse();
        response.setText(savedData.getText());
        return ResponseEntity.ok(response);
    }

    @GetMapping("")
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

    @PostMapping("/autht")
    public ResponseEntity<Boolean> trueIfAuthenticated(@RequestBody TestTAuthRequest req) {
        Boolean authed = authService.isAuthenticatedByToken(req.getCredId(), req.getToken());
        return ResponseEntity.ok(authed);
    }

    @PostMapping("/authp")
    public ResponseEntity<Boolean> trueIfAuthenticated(@RequestBody TestPAuthRequest req) {
        Boolean authed = authService.isAuthenticatedByPasshash(req.getCredId(), req.getPasswordHash());
        return ResponseEntity.ok(authed);
    }

    @GetMapping("/cat")
    public String getCatWithAuth(@RequestParam Long credId, @RequestParam String token) {
        if (authService.isAuthenticatedByToken(credId, token)) {
            return ":3";
        }
        return ":(";
    }
    
    
}
