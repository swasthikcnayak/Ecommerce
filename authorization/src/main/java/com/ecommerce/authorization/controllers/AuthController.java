package com.ecommerce.authorization.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.authorization.services.AuthService;
import com.ecommerce.authorization.utils.Constants;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/api/v1/token")
public class AuthController {

    @Autowired
    AuthService authService;
    
    // validate the token received from the client, and return the decrypted jwt with all the info
    @PostMapping(value = "/validate", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<String> validateToken(@RequestHeader(Constants.AUTHORIZATION_HEADER) String authorization) {
        Map<String, Object> value = authService.validateToken(authorization);
        return new ResponseEntity<String>(value.toString(), HttpStatus.OK);
    }

    // generate the token and return to the client
    @GetMapping(value = "/", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<String> generateToken(@RequestHeader(Constants.USER_ID_HEADER) String userId) {
        // fetch the details of the user - using user Id
        // generate the JWT token
        // return the JWT token
        String token = authService.generateToken(userId);
        return new ResponseEntity<String>(token, HttpStatus.OK);

    }
    
    
}
