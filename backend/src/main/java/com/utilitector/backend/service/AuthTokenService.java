package com.utilitector.backend.service;

import org.springframework.stereotype.Service;

import com.utilitector.backend.document.Credential;

@Service
public class AuthTokenService {
	public String makeAuthToken(Credential user) {
		return user.getId().toString(); // TODO
	}
}
