package com.example.demo.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.jwt.Jwt;

import com.example.demo.dto.request.AuthenticationRequest;
import com.example.demo.dto.response.AuthenticationResponse;
import com.example.demo.dto.response.UserResDto;
import com.example.demo.entity.Token;
import com.example.demo.entity.Users;
import com.example.demo.enums.Code;
import com.example.demo.exception.AppException;
import com.example.demo.repository.TokenRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;



@Service
public class AuthenticationService {
    
    @Value("${app.security.signer-key}")
    private String SIGNER_KEY;

    @Value("${app.security.token-expiry-hours}")
    private int tokenExpiryHours;

    @Autowired
    private UsersService userSv;

    @Autowired
    private TokenRepository tokenRepository;

    private static final Logger log = LogManager.getLogger(AuthenticationService.class);
    
    public AuthenticationResponse authenticate(AuthenticationRequest request){
        Users user = userSv.getUser(request.getUsername());
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        try {
            // check password
            if (userSv.checkPassword(request.getUsername(), request.getPassword())) {
                // generate token
                String token = generateToken(user);
                authenticationResponse.setToken(token);
            } else {
                authenticationResponse.setToken("failed");
            }
        }
        catch (RuntimeException e) {
            authenticationResponse.setToken("guest");
        }
        return authenticationResponse;
    }

    private String generateToken(Users user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        String id = UUID.randomUUID().toString();
        Date expiryTime = new Date(Instant.now().plus(tokenExpiryHours, ChronoUnit.HOURS).toEpochMilli());
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                // .issuer("meo meo")
                .issueTime(new Date())
                .expirationTime(expiryTime)
                .jwtID(id)
                .claim("scope", buildScope(user.getRoles()))
                .build();
        
        saveToken(id, expiryTime);
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);
        
        // kí token
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Token creation failed", e);
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    
        if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
    
            // Trích xuất username từ subject của JWT
            String username = jwt.getSubject(); // subject chứa username
    
            return username;
        } else {
            throw new AppException(Code.UNAUTHENTICATED);
        }
    }
    
    public int getIdUser(){
        String username = getUsernameFromToken();
        return userSv.getUserIdByUsername(username);
    }

    public Optional<UserResDto> getUser(){
        try{
            String username = getUsernameFromToken();
            return userSv.getUserByUsername(username);
        } catch (AppException e){
            return Optional.empty();
        }
    }

    public AuthenticationResponse refreshToken() {
        String tokenId = getTokenId();
        tokenRepository.deleteById(tokenId);
        String username = getUsernameFromToken();
        Users user = userSv.getUser(username);
        String newToken = generateToken(user);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setToken(newToken);
        return authenticationResponse;
    }
    
    public void logout() {
        String tokenId = getTokenId();
        tokenRepository.deleteById(tokenId);
    }

    public boolean checklogin(){
        Optional<UserResDto> user = getUser();
        if (user.isEmpty()){
            return false;
        }else{
            return true;
        }
    }
}
