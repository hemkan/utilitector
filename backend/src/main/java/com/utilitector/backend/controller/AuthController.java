package com.utilitector.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.utilitector.backend.request.CredentialRequest;
import com.utilitector.backend.response.CredentialResponse;
import com.utilitector.backend.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<CredentialResponse> registerUser(@RequestBody CredentialRequest credReq) {
        CredentialResponse response = authService.registerUser(credReq);
        if (response == null) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<CredentialResponse> loginUser(@RequestBody CredentialRequest credReq) {
        CredentialResponse response = authService.loginUser(credReq);
        if (response == null) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(response);
    }
    
}
