package com.utilitector.backend.controller;

import com.utilitector.backend.data.DCluster;
import com.utilitector.backend.logic.map.Clustering;
import com.utilitector.backend.response.CalebTestAllResponse;
import com.utilitector.backend.response.GetAllClustersResponse;
import org.apache.commons.math3.ml.clustering.Cluster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/analysis")
public class AnalysisController {
	@Autowired
	private Clustering clusterer;
	
	@GetMapping("/areas")
	public ResponseEntity<GetAllClustersResponse> getAllClusters() {
		var list = clusterer.getAllClusters()
		                    .keySet()
		                    .stream()
		                    .map(Cluster::getPoints)
		                    .map(clusterer::getCircleForCluster)
		                    .map(DCluster::from)
		                    .toList();
		
		return ResponseEntity.ok(new GetAllClustersResponse(list));
	}
	
	@GetMapping("/all")
	public ResponseEntity<Object> getAllPoints() {
		var locations = clusterer.getReports()
		                         .values()
		                         .stream()
		                         .flatMap(map -> map.values().stream())
//		                         .limit(50)
		                         .toList();
		
		var res = new CalebTestAllResponse();
		res.setLocations(locations);
		return ResponseEntity.ok(res);
	}
	
	
}
