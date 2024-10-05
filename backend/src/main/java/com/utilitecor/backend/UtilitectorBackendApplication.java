package com.utilitecor.backend;

import com.utilitecor.backend.mongo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class UtilitectorBackendApplication {
	@Autowired
	public UserRepository USERS;
	
	public static void main(String[] args) {
		SpringApplication.run(UtilitectorBackendApplication.class, args);
	}
}
