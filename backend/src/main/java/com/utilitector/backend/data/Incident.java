package com.utilitector.backend.data;

import org.springframework.data.mongodb.core.mapping.DBRef;

public class Incident {
	@DBRef
	CityListing city;
}
