package com.utilitector.backend.request;

import lombok.Data;

// This might have to be an Auth0 token
@Data
public class UserLoginRequest {
	private String authSub;

	private String email;
}
