package com.example.demo.service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.jwt.Jwt;

import com.example.demo.dto.request.AuthenticationRequest;
import com.example.demo.dto.request.IntrospectRequest;
import com.example.demo.dto.response.AuthenticationResponse;
import com.example.demo.dto.response.IntrospectResponse;
import com.example.demo.dto.response.UserResDto;
import com.example.demo.entity.Token;
import com.example.demo.exception.AppException;
import com.example.demo.repository.TokenRepository;
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
    private UsersService userSv;

    @Autowired
    private TokenRepository tokenRepository;


    public AuthenticationResponse authenticate(AuthenticationRequest request){
        Optional<UserResDto> user = userSv.getUserByUsername(request.getUsername());
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        if (user.isPresent()) {
            // check password
            if (userSv.checkPassword(request.getUsername(), request.getPassword())) {
                // generate token
                String token = generateToken(user.get());
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

    // public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
    //     var token = request.getToken();
    //     boolean isValid;
    //     try {
    //         isValid = verifyToken(token);
    //     } catch (AppException e) {
    //         isValid = false;
    //     }
    //     IntrospectResponse introspectResponse = new IntrospectResponse();
    //     introspectResponse.setValid(isValid);
    //     return introspectResponse;
    // }

    private String generateToken(UserResDto user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                // .issuer("meo meo")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.DAYS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user.getRoles()))
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

    private String buildScope(Set<String> roles) {
        StringBuilder sb = new StringBuilder();
        for (String role : roles) {
            sb.append(role).append(" ");
        }
        return sb.toString().trim();
    }

    private boolean verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        // Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        
        var verified = signedJWT.verify(verifier);

        
        return verified;
    }

    public boolean activeToken(String id) {
        Optional<Token> tokenOptional = tokenRepository.findById(id);
    
        // Nếu không tìm thấy token, trả về false
        if (tokenOptional.isEmpty()) {
            return false;
        }
    
        Token token = tokenOptional.get();
        
        // Nếu token đã hết hạn, xóa token và trả về false
        if (token.getExpiryTime().before(new Date())) {
            tokenRepository.deleteById(id);
            return false;
        }
    
        // Nếu token hợp lệ và chưa hết hạn, trả về true
        return true;
    }

    public void saveToken(String id, Date expiryTime) {
        // Tạo một đối tượng Token mới
        Token token = new Token();
        token.setId(id);
        token.setExpiryTime(expiryTime);
    
        // Lưu token vào cơ sở dữ liệu
        tokenRepository.save(token);
    }
    
    public String getTokenId() {
        // Lấy thông tin của JWT từ SecurityContext
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Trích xuất id từ claims của JWT
        String tokenId = jwt.getClaim("jti"); // "jti" là ID của token theo chuẩn JWT

        return tokenId;
    }
    
    public String getUsernameFromToken() {
        // Lấy thông tin của JWT từ SecurityContext
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    
        // Trích xuất username từ subject của JWT
        String username = jwt.getSubject(); // subject chứa username
    
        return username;
    }
}
