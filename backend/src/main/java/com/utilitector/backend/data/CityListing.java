package com.utilitector.backend.data;

import com.opencagedata.jopencage.model.JOpenCageComponents;
import com.opencagedata.jopencage.model.JOpenCageResult;
import com.utilitector.backend.document.UserData;
import com.utilitector.backend.document.UserData.UserId;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document("cities") @Data
public class CityListing {
	private String name;
	private String country;
	private String postcode;
	private @Id MercatorCoordinates coords;
	private LatitudeLongitude latLng;
	@DBRef
	private List<UserData> subscribedUsers;
	
	public CityListing(String name, String country, String postcode, MercatorCoordinates coords, LatitudeLongitude latLng) {
		this.name = name;
		this.country = country;
		this.postcode = postcode;
		this.coords = coords;
		this.latLng = latLng;
		this.subscribedUsers = List.of();
	}
	
	public static CityListing fromJOpenCageResult(JOpenCageResult res) {
		JOpenCageComponents c = res.getComponents();
		return new CityListing(c.getCity(), c.getCountry(), c.getPostcode(), MercatorCoordinates.from(res.getAnnotations().getMercator()), LatitudeLongitude.of(res.getGeometry()), new ArrayList<>(1));
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof CityListing cl && this.postcode.equals(cl.postcode) && this.name.equals(cl.name);
	}
}
