package com.utilitector.backend.service;

import com.utilitector.backend.mongo.CredentialRepository;
import com.utilitector.backend.response.UserLoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserAuthService {
	@Autowired
	private CredentialRepository userRepository;
	
	@Autowired
	private AuthTokenService authTokenService;
	
	public Optional<UserLoginResponse> logInUser(String authSub) {
		return userRepository.findCredentialByAuthSub(authSub)
		                     .map(authTokenService::makeAuthToken)
		                     .map(UserLoginResponse::new);
	}
}