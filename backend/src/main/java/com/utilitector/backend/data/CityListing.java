package com.utilitector.backend.data;

import com.opencagedata.jopencage.model.JOpenCageComponents;
import com.opencagedata.jopencage.model.JOpenCageResult;
import com.utilitector.backend.document.UserData;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Document("cities") @Data
public class CityListing {
	private String name;
	private String country;
	private String postcode;
	private @Id MercatorCoordinates coords;
	private LatitudeLongitude latLng;
	@DBRef
	private Set<UserData> subscribedUsers;
	
	public CityListing() {
		this.subscribedUsers = new HashSet<>(1);
	}
	
	public static CityListing fromJOpenCageResult(JOpenCageResult res) {
		JOpenCageComponents c = res.getComponents();
		CityListing cityListing = new CityListing();
		cityListing.setName(c.getCity());
		cityListing.setCoords(MercatorCoordinates.from(res.getAnnotations().getMercator()));
		cityListing.setCountry(c.getCountry());
		cityListing.setPostcode(c.getPostcode());
		cityListing.setLatLng(LatitudeLongitude.of(res.getGeometry()));
		return cityListing;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof CityListing cl && this.postcode.equals(cl.postcode) && this.name.equals(cl.name);
	}
}
