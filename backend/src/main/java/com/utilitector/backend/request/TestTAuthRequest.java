package com.utilitector.backend.request;

import lombok.Data;

@Data
public class TestTAuthRequest {
    private Long credId;
    private String token;
}
