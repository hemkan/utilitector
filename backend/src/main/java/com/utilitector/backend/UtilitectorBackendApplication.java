package com.utilitector.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class UtilitectorBackendApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(UtilitectorBackendApplication.class, args);
	}
}
