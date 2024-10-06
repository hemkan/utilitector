package com.utilitector.backend.service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.utilitector.backend.document.Credential;
import com.utilitector.backend.mongo.CredentialRepository;
import com.utilitector.backend.request.CredentialRequest;
import com.utilitector.backend.response.CredentialResponse;

@Service
public class AuthService {
    @Autowired private CredentialRepository credRepo;

    SecureRandom rand = new SecureRandom();

    public CredentialResponse registerUser(CredentialRequest credReq) {
        Optional<Credential> existingUserOpt = credRepo.findByUsername(credReq.getUsername());
        if (existingUserOpt.isPresent()) {
            return null;
        }
        
        Credential newUser = new Credential();
        newUser.setId(rand.nextLong());
        while (credRepo.existsById(newUser.getId())) {
            newUser.setId(rand.nextLong());
        }
        newUser.setPasswordHash(credReq.getPasswordHash());
        newUser.setUsername(credReq.getUsername());

        // Generate token
        byte[] tokenBytes = new byte[64];
        rand.nextBytes(tokenBytes);
        String tokenStringB64 = Base64.getEncoder().encodeToString(tokenBytes);
        newUser.setToken(tokenStringB64);

        // Save creds
        Credential savedCred = credRepo.save(newUser);
        CredentialResponse credResp = new CredentialResponse();
        credResp.setToken(savedCred.getToken());
        credResp.setId(savedCred.getId());
        return credResp;
    }

    public CredentialResponse loginUser(CredentialRequest credReq) {
        Optional<Credential> existingUserOpt = credRepo.findByUsername(credReq.getUsername());
        if (existingUserOpt.isPresent()) {
            return null;
        }
        
        CredentialResponse res = new CredentialResponse();
        res.setId(existingUserOpt.get().getId());
        res.setToken(existingUserOpt.get().getToken());
        return res;
    }

    public Boolean isAuthenticatedByToken(Long credId, String token) {
        Optional<Credential> credOpt = credRepo.findById(credId);
        if (credOpt.isEmpty()) {
            return false;
        }

        Credential cred = credOpt.get();
        if (cred.getToken().equals(token)) {
            return true;
        }

        return false;
    }

    public Boolean isAuthenticatedByPasshash(Long credId, String passwordHash) {
        Optional<Credential> credOpt = credRepo.findById(credId);
        if (credOpt.isEmpty()) {
            return false;
        }
        
        Credential cred = credOpt.get();
        if (cred.getPasswordHash().equals(passwordHash)) {
            return true;
        }

        return false;
    }
}
