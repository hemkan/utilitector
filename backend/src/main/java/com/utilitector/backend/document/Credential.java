package com.utilitector.backend.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document("credentials")
@Data
public class Credential {
	@Id
	private Long id;
	
	private String authSub;
}
