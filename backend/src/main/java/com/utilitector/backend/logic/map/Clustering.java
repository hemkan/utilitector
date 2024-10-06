package com.utilitector.backend.logic.map;

import com.opencagedata.jopencage.JOpenCageGeocoder;
import com.opencagedata.jopencage.model.JOpenCageResponse;
import com.opencagedata.jopencage.model.JOpenCageReverseRequest;
import com.utilitector.backend.Constants;
import com.utilitector.backend.data.CityListing;
import com.utilitector.backend.data.LatitudeLongitude;
import com.utilitector.backend.data.MercatorCoordinates;
import com.utilitector.backend.document.Report;
import com.utilitector.backend.util.Util;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math3.geometry.enclosing.EnclosingBall;
import org.apache.commons.math3.geometry.enclosing.WelzlEncloser;
import org.apache.commons.math3.geometry.euclidean.twod.DiskGenerator;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;
import org.apache.commons.math3.ml.clustering.DoublePoint;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class Clustering {
	private final SparkSession spark;
	
	@Value("${spring.opencage.api-key}")
	private String GEOCODER_KEY;
	
	private final JOpenCageGeocoder geocoder;
	
	public Clustering(@Value("${spring.data.mongodb.uri}") String MONGO_URL) {
		geocoder = new JOpenCageGeocoder(GEOCODER_KEY);
		spark = SparkSession.builder()
		                    .master("local")
		                    .config("spark.mongodb.read.connection.uri", MONGO_URL)
		                    .config("spark.mongodb.write.connection.uri", MONGO_URL)
		                    .config("spark.driver.host", "localhost")
		                    .getOrCreate();
	}
	
	@CacheEvict({Constants.CACHE_CLUSTERS, Constants.CACHE_CIRCLE})
	public void refresh() {
		// NO-OP
	}
	
	@Cacheable(Constants.CACHE_CLUSTERS)
	public Map<Cluster<DoublePoint>, List<Report>> getAllClusters() {
		
		var reportsByType = getReports();
		
		Map<Cluster<DoublePoint>, List<Report>> out = new HashMap<>();
		
		var thing = new DBSCANClusterer<DoublePoint>(0.01, 3, new LatitudeScaledDistance());
		
		for (Entry<String, Map<DoublePoint, Report>> typeToReport : reportsByType.entrySet()) {
			// convert to Cluster
			Map<DoublePoint, Report> pointToReport = typeToReport.getValue();
			
			List<DoublePoint> points = pointToReport.keySet()
			                                        .stream()
//			                                        .map(Util::toMercator)
//			                                        .map(MercatorCoordinates::toDoublePoint)
			                                        .toList();
			
			List<Cluster<DoublePoint>> clusters = thing.cluster(points);
			
			// Map the clusters to the reports they came from
			var clustersToReports = clusters.stream()
			                                .flatMap(cluster -> {
				                                return cluster.getPoints().stream().map(pt -> Pair.of(cluster, pt));
			                                })
			                                .map(pair -> Pair.of(pair.getLeft(), pointToReport.get(pair.getRight())))
			                                .collect(Collectors.groupingBy(Pair::getLeft, Collectors.mapping(Pair::getRight, Collectors.toList())));
			
			out.putAll(clustersToReports);
		}
		
		return out;
	}
	
	public @NotNull Map<String, Map<DoublePoint, Report>> getReports() {
		return getAllReports().as(Encoders.bean(Report.class))
		                      .collectAsList()
		                      .stream()
		                      .collect(Collectors.groupingBy(Report::getType, Collectors.toMap(re -> re.getLocation().toDoublePoint(), Function.identity())));
	}
	
	private Dataset<Row> getAllReports() {
		return spark.read()
		            .format("mongodb")
		            .option("database", "main")
		            .option("collection", Constants.DB_REPORTS_NAME)
		            .load();
	}
	
	@Cacheable(Constants.CACHE_CIRCLE)
	public EnclosingBall<Euclidean2D, Vector2D> getCircleForCluster(Collection<DoublePoint> c) {
		List<Vector2D> list = c.stream().map(p -> new Vector2D(p.getPoint())).toList();
		var welzl = new WelzlEncloser<>(0.01, new DiskGenerator());
		return welzl.enclose(list);
	}
	
	
	public CityListing closestCity(LatitudeLongitude latLng) {
		var req = new JOpenCageReverseRequest(latLng.getLatitude(), latLng.getLongitude());
		JOpenCageResponse reverse = geocoder.reverse(req);
		
		reverse.orderResultByConfidence();
		if (reverse.getResults().isEmpty())
			return null;
		
		var first = reverse.getResults().getFirst();
		return CityListing.fromJOpenCageResult(first);
	}
	
	public class LatitudeScaledDistance implements DistanceMeasure {
		
		@Override
		public double compute(double[] point1, double[] point2) {
			// Assuming points are in [longitude, latitude] format
			double lon1 = point1[0];
			double lat1 = point1[1];
			double lon2 = point2[0];
			double lat2 = point2[1];
			
			// Compute horizontal and vertical distance components
			double deltaLon = lon1 - lon2;
			double deltaLat = lat1 - lat2;
			
			// Scale horizontal distance component by latitude
			double scaleFactor = Math.cos(Math.toRadians((lat1 + lat2) / 2));
			double scaledDeltaLon = deltaLon * scaleFactor;
			
			// Calculate Euclidean distance with scaled longitude component
			return Math.sqrt(scaledDeltaLon * scaledDeltaLon + deltaLat * deltaLat);
		}
	}
}
