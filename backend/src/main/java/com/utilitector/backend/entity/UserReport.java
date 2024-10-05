package com.utilitector.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class UserReport {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    private String type;

    private String description;
    
    private String location; //TODO
}
