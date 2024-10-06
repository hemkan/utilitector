package com.utilitector.backend.data;

import com.opencagedata.jopencage.model.JOpenCageMercator;

public record MercatorCoordinates(double x, double y) {
	public static MercatorCoordinates of(JOpenCageMercator merc) {
		return new MercatorCoordinates(merc.getX(), merc.getY());
	}
}
