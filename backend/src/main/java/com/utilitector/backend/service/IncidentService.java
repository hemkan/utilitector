package com.utilitector.backend.service;

import com.utilitector.backend.data.CityListing;
import com.utilitector.backend.data.Incident;
import com.utilitector.backend.data.MercatorCoordinates;
import com.utilitector.backend.document.Report;
import com.utilitector.backend.document.UserData;
import com.utilitector.backend.logic.map.Clustering;
import com.utilitector.backend.mongo.IncidentsRepository;
import com.utilitector.backend.mongo.UserDataRepository;
import com.utilitector.backend.mongo.locations.CitiesRepository;
import com.utilitector.backend.util.Util;
import org.apache.commons.math3.geometry.enclosing.EnclosingBall;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.DoublePoint;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class IncidentService {
	@Autowired
	private IncidentsRepository repo;
	
	public void onIncident(Incident incident) {
		repo.insert(incident);
		if (!cityRepo.existsById(incident.getCity().getCoords())) {
			cityRepo.insert(incident.getCity());
		} else {
			cityRepo.findCityListingByCoords(incident.getCity().getCoords())
			        .getSubscribedUsers()
			        .forEach(user -> user.getSubscribedIncidents().add(incident));
		}
	}
	
	@Autowired
	private Clustering clusterService;
	
	@Autowired
	private CitiesRepository cityRepo;
	
	public void triggerIncidentCheck() {
		clusterService.refresh();
		repo.deleteAll();
//		Set<Incident> allIncidents = repo.getAllByTimeNear(Instant.now());
		clusterService.getAllClusters()
		              .entrySet()
		              .stream()
		              .map(entry -> createIncident(entry.getKey(), entry.getValue()))
		              .forEach(this::onIncident);
	}
	
	public Incident createIncident(Cluster<DoublePoint> cluster, List<Report> cause) {
		EnclosingBall<Euclidean2D, Vector2D> circle = clusterService.getCircleForCluster(cluster.getPoints());
		var latitudeLongitude = Util.fromMercator(MercatorCoordinates.from(circle.getCenter()));
		Incident incident = new Incident();
		incident.setTriggeredBy(new HashSet<>(cause));
		incident.setCity(clusterService.closestCity(latitudeLongitude));
		incident.setType(cause.getFirst().getType());
		return incident;
	}
	
}
