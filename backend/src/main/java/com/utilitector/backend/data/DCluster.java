package com.utilitector.backend.data;

import com.utilitector.backend.util.Util;
import org.apache.commons.math3.geometry.enclosing.EnclosingBall;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public record DCluster(double radius, LatitudeLongitude center, long numContained) {
	
	public static DCluster from(EnclosingBall<Euclidean2D, Vector2D> ball) {
		return new DCluster(ball.getRadius(), Util.fromMercator(MercatorCoordinates.from(ball.getCenter())), ball.getSupportSize());
	}
}
