package com.ecommerce.users.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ecommerce.users.FeignConfig;
import com.ecommerce.users.dto.UserRegistrationRequest;

@FeignClient(name="authservice", configuration = FeignConfig.class)
public interface Authclient {

    @PostMapping(path = "/auth/v1/register")
    public ResponseEntity<Void> register(
        @RequestBody UserRegistrationRequest  userRegistrationDto);
    
}
