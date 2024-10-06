package com.utilitector.backend.util;

import com.utilitector.backend.data.LatitudeLongitude;
import com.utilitector.backend.data.MercatorCoordinates;
import org.apache.commons.math3.ml.clustering.DoublePoint;

public final class Util {
	// Radius of the Earth (in meters)
	private static final double EARTH_RADIUS = 6378137;
	
	/**
	 * Converts latitude and longitude to Mercator x, y coordinates.
	 *
	 * @param latitude  Latitude in degrees.
	 * @param longitude Longitude in degrees.
	 * @return A double array with x and y coordinates.
	 */
	public static MercatorCoordinates latLonToMercator(double latitude, double longitude) {
		// Convert longitude to x coordinate
		double x = EARTH_RADIUS * Math.toRadians(longitude);
		
		// Convert latitude to y coordinate using the Mercator projection formula
		double y = EARTH_RADIUS * Math.log(Math.tan(Math.PI / 4 + Math.toRadians(latitude) / 2));
		
		return new MercatorCoordinates(x, y);
	}
	
	public static LatitudeLongitude mercatorToLatLon(double x, double y) {
		double lon = x / EARTH_RADIUS;
		double lat = Math.toDegrees(Math.atan(Math.sinh(y / EARTH_RADIUS)));
		return LatitudeLongitude.of(lat, lon);
	}
	
	public static MercatorCoordinates toMercator(DoublePoint pt) {
		return latLonToMercator(pt.getPoint()[0], pt.getPoint()[1]);
	}
	
	public static MercatorCoordinates toMercator(LatitudeLongitude ll) {
		return latLonToMercator(ll.getLatitude(), ll.getLongitude());
	}
	
	
	public static LatitudeLongitude fromMercator(MercatorCoordinates coords) {
		return mercatorToLatLon(coords.x(), coords.y());
	}
}
