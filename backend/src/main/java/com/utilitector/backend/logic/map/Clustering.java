package com.utilitector.backend.logic.map;

import com.utilitector.backend.data.GeolocationCoordinates;
import org.apache.spark.ml.clustering.KMeans;
import org.apache.spark.ml.clustering.KMeansModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
		                               .option("collection", "UserReports")
		                               .load();
		
		var reportLocations = reportData.select("location")
		                                .as(Encoders.bean(GeolocationCoordinates.class));
		
		System.out.println(reportLocations);
		
		System.exit(0);
		
		KMeans kMeans = new KMeans().setK(3).setSeed(1L);
		KMeansModel model = kMeans.fit(reportData);
		
		Dataset<Row> transform = model.transform(reportData);
	}
}
