package com.ecommerce.users.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.users.dto.UserRegistrationDto;
import com.ecommerce.users.services.UserService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;




@RestController
@RequestMapping("users/v1/")
public class UserController {
    
    @Autowired
    UserService userService;

    @PostMapping(name = "createUser", value="/", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public String registerUser(@RequestBody UserRegistrationDto UserRegistrationDto) {
        return "complete";
    }

    @DeleteMapping(name = "deleteUser", value = "/", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public String deleteUser(){
        return "deleted";
    }

    @GetMapping(name="getUser", value = "/",produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public String getUser() {
        return "get request";
    }
    
    @PatchMapping(name="updateUser", value="/",produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public String updateUser() {
        return "update request";
    }


}
