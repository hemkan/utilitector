package com.utilitector.backend.service;

import com.utilitector.backend.entity.User;
import org.springframework.stereotype.Service;

@Service
public class AuthTokenService {
	public String makeAuthToken(User user) {
		return user.getId().toString(); // TODO
	}
}
