package com.utilitector.backend.request;

import lombok.Data;

@Data
public class TestPAuthRequest {
    private Long credId;
    private String passwordHash;
}
