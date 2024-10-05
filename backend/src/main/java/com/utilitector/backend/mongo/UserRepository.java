package com.utilitector.backend.mongo;

import com.utilitector.backend.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, Long> {
	
//	@Query("{username:'?0', passwordHash: '?1'}")
	Optional<User> findUserByUsernameAndPasswordHash(String username, String passwordHash);
}