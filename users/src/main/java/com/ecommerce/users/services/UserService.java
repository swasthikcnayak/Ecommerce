package com.ecommerce.users.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ecommerce.users.clients.Authclient;
import com.ecommerce.users.dto.UserRegistrationRequest;

@Service
public class UserService {

    @Autowired
    Authclient authclient;
    
    public ResponseEntity<Void> registerUser(UserRegistrationRequest userRegistrationDto) {
        authclient.register(userRegistrationDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
