package com.utilitector.backend.service;

import com.utilitector.backend.data.CityListing;
import com.utilitector.backend.mongo.UserDataRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IncidentService {
	@Autowired
	private UserDataRepository repo;
	
	public void onIncident(CityListing city, Incident incident) {
		for (ObjectId userId : city.subscribedUsers()) {
			repo.getUserDataById(userId).getSubscribedIncidents().add(incident);
		}
	}
}
