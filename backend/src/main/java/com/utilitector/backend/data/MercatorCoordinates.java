package com.utilitector.backend.data;

import com.opencagedata.jopencage.model.JOpenCageMercator;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.ml.clustering.DoublePoint;

public record MercatorCoordinates(double x, double y) {
	public static MercatorCoordinates from(JOpenCageMercator merc) {
		return new MercatorCoordinates(merc.getX(), merc.getY());
	}
	
	public static MercatorCoordinates from(DoublePoint merc) {
		return new MercatorCoordinates(merc.getPoint()[0], merc.getPoint()[1]);
	}
	
	public static MercatorCoordinates from(Vector2D merc) {
		return new MercatorCoordinates(merc.getX(), merc.getY());
	}
	
	public DoublePoint toDoublePoint() {
		return new DoublePoint(new double[] {this.x(), this.y()});
	}
	
}
