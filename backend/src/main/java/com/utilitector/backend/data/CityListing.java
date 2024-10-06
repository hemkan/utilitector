package com.utilitector.backend.data;

import com.opencagedata.jopencage.model.JOpenCageComponents;
import com.opencagedata.jopencage.model.JOpenCageResult;

public record CityListing(
		String name,
		String country,
		String postcode,
		MercatorCoordinates coords,
		LatitudeLongitude latLng
) {
	public static CityListing fromJOpenCageResult(JOpenCageResult res) {
		JOpenCageComponents c = res.getComponents();
		return new CityListing(c.getCity(), c.getCountry(), c.getPostcode(), MercatorCoordinates.of(res.getAnnotations().getMercator()), LatitudeLongitude.of(res.getGeometry()));
	}
}
