package com.utilitecor.backend.mongo;

import com.utilitecor.backend.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, Long> {
	
//	@Query("{username:'?0', passwordHash: '?1'}")
	Optional<User> findUserByUsernameAndPasswordHash(String username, String passwordHash);
}
