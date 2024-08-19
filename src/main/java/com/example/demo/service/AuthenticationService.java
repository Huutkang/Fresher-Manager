package com.example.demo.service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.request.AuthenticationRequest;
import com.example.demo.dto.request.IntrospectRequest;
import com.example.demo.dto.response.AuthenticationResponse;
import com.example.demo.dto.response.IntrospectResponse;
import com.example.demo.exception.AppException;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.experimental.NonFinal;




@Service
public class AuthenticationService {
    @NonFinal
    private final static String SIGNER_KEY = "UqPgTaQLnqjwuOJ54TZnQekWcLyA+eR68BBKTULU/hD3IdIk5aHani1twPPQhlXf";
    @Autowired
    private UsersService user;

    public AuthenticationResponse authenticate(AuthenticationRequest request){
        Optional<Integer> id = user.getUserIdByUsername(request.getUsername());
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        if (id.isPresent()) {
            // check password
            if (user.checkPassword(id.get(), request.getPassword())) {
                // generate token
                String token = generateToken(request.getUsername());
                authenticationResponse.setToken(token);
            } else {
                authenticationResponse.setToken("failed");
            }
        }
        else{
            authenticationResponse.setToken("guest");
        }
        return authenticationResponse;
    }

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();
        boolean isValid;
        try {
            isValid = verifyToken(token);
        } catch (AppException e) {
            isValid = false;
        }
        IntrospectResponse introspectResponse = new IntrospectResponse();
        introspectResponse.setValid(isValid);
        return introspectResponse;
    }

    private String generateToken(String username) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                // .issuer("meo meo")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                // .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);
        
        // kí token
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            // log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }

    private boolean verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        // Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        
        var verified = signedJWT.verify(verifier);

        
        return verified;
    }
}
