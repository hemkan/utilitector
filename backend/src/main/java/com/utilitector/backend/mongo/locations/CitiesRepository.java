package com.utilitector.backend.mongo.locations;

import com.utilitector.backend.data.CityListing;
import com.utilitector.backend.data.MercatorCoordinates;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

public interface CitiesRepository extends MongoRepository<CityListing, MercatorCoordinates> {
	CityListing findCityListingByCoords(MercatorCoordinates coords);
}
