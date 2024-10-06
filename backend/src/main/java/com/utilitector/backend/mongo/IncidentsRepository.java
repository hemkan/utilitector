package com.utilitector.backend.mongo;

import com.utilitector.backend.data.Incident;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.Set;

public interface IncidentsRepository extends MongoRepository<Incident, ObjectId> {
	Set<Incident> getAllByTimeNear(Instant time);
}
