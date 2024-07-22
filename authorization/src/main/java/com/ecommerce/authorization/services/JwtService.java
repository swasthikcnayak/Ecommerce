package com.ecommerce.authorization.services;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.crypto.SecretKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.ecommerce.authorization.utils.Constants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private static final Logger LOG = LoggerFactory.getLogger(JwtService.class);

    private Environment environment;

    private JwtParser jwtParser;

    private SecretKey signInKey;

    private long expiration;

    public JwtService(Environment environment) {
        this.environment = environment;
        this.initJwt();
    }

    public Map<String, Object> extractJwt(String token, List<String> claimNames) {
        return this.extractToken(this.jwtParser, token, claimNames);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            String userId) {
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(userId)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + this.expiration))
                .signWith(this.signInKey)
                .compact();
    }

    private Map<String, Object> extractToken(JwtParser parser, String jwtToken, List<String> claimNames) {
        Map<String, Object> returnValue = new HashMap<>();
        try {
            Jwt<?, ?> jwtObject = parser.parse(jwtToken);
            Object payload = jwtObject.getPayload();

            if (payload instanceof Claims claims) {
                for(String claim : claimNames){
                    returnValue.put(claim, claims.get(claim, String.class));
                }
                        }
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return returnValue;
    }

    private JwtParser initJwt() {
        this.expiration = Long.parseLong(Objects.requireNonNull(environment.getProperty(Constants.JWT_EXIPIY)));
        String secret = Objects.requireNonNull(environment.getProperty(Constants.JWT_SECRET_KEY));
        byte[] secretBytes = Base64.getEncoder().encode(secret.getBytes());
        this.signInKey = Keys.hmacShaKeyFor(secretBytes);
        JwtParser jwtParser = Jwts.parser()
                .verifyWith(this.signInKey)
                .build();
        return jwtParser;
    }

}

