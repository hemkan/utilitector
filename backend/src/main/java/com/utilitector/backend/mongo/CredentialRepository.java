package com.utilitector.backend.mongo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.utilitector.backend.document.Credential;

public interface CredentialRepository extends MongoRepository<Credential, Long> {
    Optional<Credential> findByUsername(String username);
}
