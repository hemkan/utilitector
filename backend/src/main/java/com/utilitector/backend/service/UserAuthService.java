package com.utilitector.backend.service;

import com.utilitector.backend.mongo.UserRepository;
import com.utilitector.backend.response.UserLoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserAuthService {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AuthTokenService authTokenService;
	
	public Optional<UserLoginResponse> logInUser(String username, String passwordPlaintext) {
		return userRepository.findUserByUsernameAndPasswordHash(username, passwordPlaintext) // TODO hash password i guess, idk if anyone's gonna check
		                     .map(authTokenService::makeAuthToken)
		                     .map(UserLoginResponse::new);
	}
}
