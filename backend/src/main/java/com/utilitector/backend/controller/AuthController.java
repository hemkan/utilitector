package com.utilitector.backend.controller;

import com.utilitector.backend.request.UserLoginRequest;
import com.utilitector.backend.response.UserLoginResponse;
import com.utilitector.backend.service.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/attemptlogin")
public class AuthController {
	@Autowired private UserAuthService authService;
	
	@PostMapping("")
	public ResponseEntity<UserLoginResponse> onLoginRequest(@RequestBody UserLoginRequest request) {
		return authService.logInUser(request.getUsername(), request.getPassword())
		                  .map(ResponseEntity::ok)
		                  .orElseGet(() -> ResponseEntity.notFound().build());
	}
}
