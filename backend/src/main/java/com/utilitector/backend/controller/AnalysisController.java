package com.utilitector.backend.controller;

import com.utilitector.backend.data.Cluster;
import com.utilitector.backend.data.MercatorCoordinates;
import com.utilitector.backend.logic.map.Clustering;
import com.utilitector.backend.response.GetAllClustersResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/analysis")
public class AnalysisController {
	@Autowired
	private Clustering clusterer;
	
	@GetMapping("/clusters")
	public ResponseEntity<GetAllClustersResponse> getAllClusters() {
		var list = clusterer.getAllClusters()
		                    .stream()
		                    .map(clusterer::getCircleForCluster)
		                    .map(ball -> new Cluster(ball.getRadius(), MercatorCoordinates.from(ball.getCenter()).toLatitudeLongitude(), ball.getSupportSize()))
		                    .toList();
		
		return ResponseEntity.ok(new GetAllClustersResponse(list));
	}
	
	
}
