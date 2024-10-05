package com.utilitector.backend.response;

import java.util.List;

import com.utilitector.backend.document.AreaMonitor;

import lombok.Data;

@Data
public class UserDataResponse {
    private String email;

    private String name;

    private List<AreaMonitor> monitoring;
}
