package com.utilitector.backend.logic.map;

import com.utilitector.backend.data.GeolocationCoordinates;
import com.utilitector.backend.document.Report;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.ml.clustering.KMeans;
import org.apache.spark.ml.clustering.KMeansModel;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.ml.param.Param;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.catalyst.ScalaReflection;
import org.apache.spark.sql.types.StructType;
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
		                               .option("collection", "reports")
		                               .load();
		spark.stop();
		
		var reportLocations = reportData.as(Encoders.bean(Report.class))
		                                .map((MapFunction<Report, GeolocationCoordinates>) Report::getLocation, Encoders.bean(GeolocationCoordinates.class));
		
		var transformer = new VectorAssembler().setInputCols(new String[] {"latitude", "longitude"}).setOutputCol("features");
		Dataset<Row> transformed = transformer.transform(reportLocations);
		
		System.out.println(transformed);
		
		KMeans kMeans = new KMeans().setK(3).setSeed(1L);
		
		KMeansModel model = kMeans.fit(transformed);
		
		Dataset<Row> transform = model.transform(transformed);
		
		System.out.println(transform);
	}
}
