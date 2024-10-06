package com.utilitector.backend;

import com.utilitector.backend.data.CityListing;
import com.utilitector.backend.document.UserData.UserId;
import com.utilitector.backend.mongo.UserDataRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

public class Constants {
	public static final String DB_REPORTS_NAME = "reports";
	public static final String CACHE_CLUSTERS = "cache_reports";
	public static final String CACHE_CIRCLE = "cache_circles";
}
