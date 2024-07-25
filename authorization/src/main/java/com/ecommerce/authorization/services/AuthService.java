package com.ecommerce.authorization.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.authorization.dao.User;
import com.ecommerce.authorization.dao.projections.AuthInfo;
import com.ecommerce.authorization.dao.projections.UserInfo;
import com.ecommerce.authorization.dto.request.UserLoginRequest;
import com.ecommerce.authorization.dto.request.UserRegistrationRequest;
import com.ecommerce.authorization.dto.response.AuthResource;
import com.ecommerce.authorization.dto.response.TokenResource;
import com.ecommerce.authorization.dto.response.UserRegistrationResponse;
import com.ecommerce.authorization.repository.UserRepository;
import com.ecommerce.authorization.utils.ObjectUtils;

import io.jsonwebtoken.Claims;

@Service
public class AuthService {

    @Autowired
    SecretService secretService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ObjectUtils objectUtils;

    public UserRegistrationResponse register(UserRegistrationRequest registrationRequest){
        String passwordHash = this.secretService.getPasswordHash(registrationRequest.getPassword());
        User user = User.builder()
            .email(registrationRequest.getEmail().toLowerCase())
            .password(passwordHash)
            .isVerified(false)
            .lastLoggedIn(null)
            .build();
        user = this.userRepository.save(user);
        return new UserRegistrationResponse(user.getId().toString(), user.getEmail());
    }

    public AuthResource login(UserLoginRequest userLoginRequest){
        AuthInfo auth = userRepository.findOneByEmailAndIsVerified(userLoginRequest.getEmail(), true);
        if(auth == null){
            // throw authentication exception
            return null;
        }
        boolean isValid = secretService.comparePassword(userLoginRequest.getPassword(), auth.getPassword());
        if(isValid == false){
            // throw authentication exception
            return null;
        }
        Map<String, Object> claims = objectUtils.getClaims((UserInfo)auth);
        String token = secretService.generateToken(auth.getId().toString(),claims);
        return new AuthResource(auth.getEmail(), token);
    }

    public TokenResource validateToken(String token) {
        if (token == null || token.isEmpty()) {
            // throw authentication exception
            return null;
        }
        if (token.startsWith("Bearer")) {
            token = token.substring(6).trim().strip();
        }
        Object payload = secretService.extractJwt(token);
        if (payload instanceof Claims) {
            TokenResource tokenResource = objectUtils.getTokenResource(objectUtils.getMapFromClaims((Claims)payload));
            return tokenResource;
        }
        return null;
    }

}
