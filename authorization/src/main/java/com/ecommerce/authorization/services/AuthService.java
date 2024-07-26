package com.ecommerce.authorization.services;

import java.util.Date;
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
import com.ecommerce.authorization.utils.errors.AuthenticationException;
import com.ecommerce.authorization.utils.errors.BadRequestException;

import io.jsonwebtoken.Claims;

@Service
public class AuthService {

    @Autowired
    SecretService secretService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ObjectUtils objectUtils;

    public UserRegistrationResponse register(UserRegistrationRequest registrationRequest) throws BadRequestException {
        String passwordHash = this.secretService.getPasswordHash(registrationRequest.getPassword());
        User user = User.builder()
            .email(registrationRequest.getEmail().toLowerCase())
            .password(passwordHash)
            .isVerified(false)
            .lastLoggedIn(null)
            .build();
        try{ 
            user = this.userRepository.save(user);
        }catch(Exception e){
            throw new BadRequestException("Account already exists");
        }
        return new UserRegistrationResponse(user.getId().toString(), user.getEmail());
    }

    public AuthResource login(UserLoginRequest userLoginRequest) throws AuthenticationException{
        AuthInfo auth = userRepository.findOneByEmailAndIsVerified(userLoginRequest.getEmail(), true);
        if(auth == null){
            throw new AuthenticationException("Wrong email/password or email not verified");
        }
        boolean isValid = secretService.comparePassword(userLoginRequest.getPassword(), auth.getPassword());
        if(isValid == false){
            throw new AuthenticationException("Wrong email/password");
        }
        Map<String, Object> claims = objectUtils.getClaims((UserInfo)auth);
        String token = secretService.generateToken(auth.getId().toString(),claims);
        return new AuthResource(auth.getEmail(), token);
    }

    public TokenResource validateToken(String token) throws BadRequestException, AuthenticationException {
        if (token == null || token.isEmpty()) {
            throw new BadRequestException("Token cannot be empty");
        }
        if (token.startsWith("Bearer")) {
            token = token.substring(6).trim().strip();
        }
        Object payload = secretService.extractJwt(token);
        if (payload instanceof Claims) {
            TokenResource tokenResource = objectUtils.getTokenResource(objectUtils.getMapFromClaims((Claims)payload));
            if(tokenResource.getExpiry().before(new Date())){
                throw new AuthenticationException("Token expired");
            }
            return tokenResource;
        }
        throw new BadRequestException("Invalid token");
    }

}
