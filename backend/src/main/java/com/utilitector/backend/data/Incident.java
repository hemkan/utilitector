package com.utilitector.backend.data;

import com.utilitector.backend.document.Report;
import lombok.Data;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Document("incidents") @Data
public class Incident {
	@Id
	private ObjectId _id;
	
	@DBRef
	private CityListing city;
	
	private Instant time = Instant.now();
	
	private String type;
	
	@DBRef
	private Set<Report> triggeredBy = new HashSet<>();
}
