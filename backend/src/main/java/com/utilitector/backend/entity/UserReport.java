package com.utilitector.backend.entity;

import lombok.Data;

@Data
public class UserReport {

    private Long id;

    private String type;

    private String description;
    
    private String location; //TODO
}
