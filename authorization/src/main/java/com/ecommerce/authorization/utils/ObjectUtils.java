package com.ecommerce.authorization.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;

import com.ecommerce.authorization.dao.projections.UserInfo;
import com.ecommerce.authorization.dto.response.TokenResource;
import io.jsonwebtoken.Claims;

@Component
public class ObjectUtils {

    public Map<String, Object> getClaims(UserInfo userInfo) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(Constants.EMAIL_TOKEN, userInfo.getEmail());
        return claims;
    }

    public TokenResource getTokenResource(Map<String, Object> claims) {
        return TokenResource.builder()
                .email((String) claims.get(Constants.EMAIL_TOKEN))
                .expiry(new Date(((Long)claims.get(Constants.EXPIRY_TOKEN))*1000))
                .id((String) claims.get(Constants.SUBJECT_TOKEN))
                .issuedAt(new Date(((Long)claims.get(Constants.ISSUED_AT_TOKEN))*1000))
                .build();
    }

    public Map<String, Object> getMapFromClaims(Claims claims) {
        Map<String, Object> map = new HashMap<String, Object>();
        for (Entry<String, Object> entry : claims.entrySet()) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }

}
