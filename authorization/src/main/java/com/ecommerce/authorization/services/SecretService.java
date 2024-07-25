package com.ecommerce.authorization.services;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

import javax.crypto.SecretKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecommerce.authorization.utils.Constants;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class SecretService {

    private static final Logger LOG = LoggerFactory.getLogger(SecretService.class);

    private Environment environment;

    private JwtParser jwtParser;

    private SecretKey signInKey;

    private long expiration;

    private int passwordStrength;

    public SecretService(Environment environment) {
        this.environment = environment;
        this.initJwt();
        this.initPasswordConfig();
    }

    public Object extractJwt(String token) {
        return this.extractToken(this.jwtParser, token);
    }

    public String generateToken(
            String userId,
            Map<String, Object> claims) {
        return Jwts
                .builder()
                .claims(claims)
                .subject(userId)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + this.expiration))
                .signWith(this.signInKey)
                .compact();
    }

    private Object extractToken(JwtParser parser, String jwtToken) {
        try {
            Jwt<?, ?> jwtObject = parser.parse(jwtToken);
            Object payload = jwtObject.getPayload();
            return payload;
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return null;
    }

    public String getPasswordHash(String password) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(this.passwordStrength,
                new SecureRandom());
        String encodedPassword = bCryptPasswordEncoder.encode(password);
        return encodedPassword;
    }

    public boolean comparePassword(String password, String hash){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(this.passwordStrength, new SecureRandom());
        return bCryptPasswordEncoder.matches(password, hash);
    }

    private void initJwt() {
        this.expiration = Long.parseLong(Objects.requireNonNull(environment.getProperty(Constants.JWT_EXIPIY)));
        String secret = Objects.requireNonNull(environment.getProperty(Constants.JWT_SECRET_KEY));
        byte[] secretBytes = Base64.getEncoder().encode(secret.getBytes());
        this.signInKey = Keys.hmacShaKeyFor(secretBytes);
        this.jwtParser = Jwts.parser()
                .verifyWith(this.signInKey)
                .build();
    }

    private void initPasswordConfig() {
        this.passwordStrength = Integer
                .parseInt(Objects.requireNonNull(environment.getProperty(Constants.PASSWORD_STRENGTH)));
    }

}
