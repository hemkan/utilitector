package com.utilitector.backend.controller;

import com.utilitector.backend.mongo.UserDataRepository;
import com.utilitector.backend.response.dashboard.SubscribedIncidentsPayload;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
	@Autowired
	private UserDataRepository repo;
	
    @PostMapping("/subscriptions")
	public ResponseEntity<SubscribedIncidentsPayload> getSubscribedIncidents(@CookieValue String user_id) {
	    return ResponseEntity.ok(new SubscribedIncidentsPayload(repo.getUserDataBy_id(new ObjectId(user_id)).getSubscribedIncidents()));
    }

	@GetMapping("/subscriptions/force")
	public ResponseEntity<SubscribedIncidentsPayload> dofff(@CookieValue String user_id) {
		
		return getSubscribedIncidents(user_id);
	}
}
