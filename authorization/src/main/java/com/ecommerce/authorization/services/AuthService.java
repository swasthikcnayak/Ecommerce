package com.ecommerce.authorization.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    JwtService jwtService;

    public Map<String, Object> validateToken(String token){
        if(token == null || token.isEmpty()){
            return null;
        }
        if( token.startsWith("Bearer")){
            token = token.substring(6).trim().strip();
        }
        List<String> claims = new ArrayList<>();
        claims.add("userId");
        return jwtService.extractJwt(token, claims);
    }

    public String generateToken(String userId){
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        return jwtService.generateToken(claims, userId);
    }


}
