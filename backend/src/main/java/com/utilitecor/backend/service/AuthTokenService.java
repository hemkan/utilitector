package com.utilitecor.backend.service;

import com.utilitecor.backend.entity.User;
import org.springframework.stereotype.Service;

@Service
public class AuthTokenService {
	public String makeAuthToken(User user) {
		return user.getId().toString(); // TODO
	}
}
