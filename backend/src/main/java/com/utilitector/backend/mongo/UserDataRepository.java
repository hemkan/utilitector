package com.utilitector.backend.mongo;

import com.utilitector.backend.document.UserData;
import com.utilitector.backend.document.UserData.UserId;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserDataRepository extends MongoRepository<UserData, UserId> {
	UserData getUserDataBy_id(ObjectId id);
}
