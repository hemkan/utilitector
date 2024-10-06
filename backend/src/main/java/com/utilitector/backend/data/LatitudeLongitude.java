package com.utilitector.backend.data;

import com.opencagedata.jopencage.model.JOpenCageLatLng;
import lombok.Data;
import org.apache.commons.math3.ml.clustering.DoublePoint;

@Data
public class LatitudeLongitude {
	private double latitude;
	private double longitude;
	
	public DoublePoint toDoublePoint() {
		return new DoublePoint(new double[] { this.latitude, this.longitude });
	}
	
	public static LatitudeLongitude of(JOpenCageLatLng ll) {
		LatitudeLongitude latitudeLongitude = new LatitudeLongitude();
		latitudeLongitude.setLatitude(ll.getLat());
		latitudeLongitude.setLongitude(ll.getLng());
		return latitudeLongitude;
	}
	
	public static LatitudeLongitude of(double latitude,double longitude) {
		var ret = new LatitudeLongitude();
		ret.setLatitude(latitude);
		ret.setLongitude(longitude);
		return ret;
	}
}
