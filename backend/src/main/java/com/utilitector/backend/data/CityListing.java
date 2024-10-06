package com.utilitector.backend.data;

import com.opencagedata.jopencage.model.JOpenCageComponents;
import com.opencagedata.jopencage.model.JOpenCageResult;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("cities")
public record CityListing(
		String name,
		String country,
		String postcode,
		@Id MercatorCoordinates coords,
		LatitudeLongitude latLng
) {
	public static CityListing fromJOpenCageResult(JOpenCageResult res) {
		JOpenCageComponents c = res.getComponents();
		return new CityListing(c.getCity(), c.getCountry(), c.getPostcode(), MercatorCoordinates.from(res.getAnnotations().getMercator()), LatitudeLongitude.of(res.getGeometry()));
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof CityListing cl && this.postcode().equals(cl.postcode()) && this.name().equals(cl.name());
	}
}
