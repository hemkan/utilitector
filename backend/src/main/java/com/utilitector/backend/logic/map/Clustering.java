package com.utilitector.backend.logic.map;

import com.opencagedata.jopencage.JOpenCageGeocoder;
import com.opencagedata.jopencage.model.JOpenCageResponse;
import com.opencagedata.jopencage.model.JOpenCageReverseRequest;
import com.utilitector.backend.Constants;
import com.utilitector.backend.data.CityListing;
import com.utilitector.backend.data.LatitudeLongitude;
import com.utilitector.backend.data.MercatorCoordinates;
import com.utilitector.backend.document.Report;
import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;
import org.apache.commons.math3.ml.clustering.DoublePoint;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Clustering {
	
	@Value("${spring.data.mongodb.uri}")
	private String MONGO_URL;
	
	private SparkSession spark;
	
	public void doSparkThing() {
		if (spark == null)
			spark = SparkSession.builder()
			                    .master("local")
			                    .config("spark.mongodb.read.connection.uri", MONGO_URL)
			                    .config("spark.mongodb.write.connection.uri", MONGO_URL)
			                    .getOrCreate();
		
		
		Dataset<Row> reportData = spark.read()
		                               .format("mongodb")
		                               .option("database", "main")
		                               .option("collection", Constants.DB_REPORTS_NAME)
		                               .load();
		
		var reportLocations = reportData.as(Encoders.bean(Report.class))
		                                .map((MapFunction<Report, LatitudeLongitude>) Report::getLocation, Encoders.bean(LatitudeLongitude.class))
		                                .toJavaRDD()
		                                .map(e -> new DoublePoint(new double[] {e.getLatitude(), e.getLongitude()}))
		                                .collect();
		
		var thing = new DBSCANClusterer<DoublePoint>(0.4D, 5);
		List<Cluster<DoublePoint>> cluster = thing.cluster(reportLocations);
		
		
		//		var transformer = new VectorAssembler().setInputCols(new String[] {"latitude", "longitude"}).setOutputCol("features");
//		Dataset<Row> transformed = transformer.transform(reportLocations);
//
//		System.out.println(transformed);
//
//		KMeans kMeans = new KMeans().setK(3).setSeed(1L);
//
//		KMeansModel model = kMeans.fit(transformed);
//
//		Dataset<Row> transform = model.transform(transformed);
//
//		System.out.println(Arrays.toString(model.clusterCenters()));
	}
	
	@Value("${spring.opencage.api-key}")
	private String GEOCODER_KEY;
	
	private final JOpenCageGeocoder geocoder;
	
	public Clustering() {
		geocoder = new JOpenCageGeocoder(GEOCODER_KEY);
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
}
