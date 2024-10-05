package com.utilitector.backend.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.utilitector.backend.document.Credential;

import java.util.Optional;

@Repository
public interface CredentialRepository extends MongoRepository<Credential, Long> {
	
//	@Query("{username:'?0', passwordHash: '?1'}")
	Optional<Credential> findCredentialByAuthSub(String authSub);
}
