package com.utilitector.backend.request;

import lombok.Data;

@Data
public class CredentialRequest {
    private String username;

    private String passwordHash;
}
