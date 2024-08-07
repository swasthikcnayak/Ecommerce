package com.ecommerce.users;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.ecommerce.users.utils.Constants;

import feign.Request;

@Configuration
public class FeignConfig {

    @Autowired
    private Environment environment;

    @Bean
    Request.Options requestOptions() {
        long connectionTimeout = Long
                .parseLong(Objects.requireNonNull(environment.getProperty(Constants.FEIGN_CONNECTION_TIMEOUT)));
        long readTimeout = Long
                .parseLong(Objects.requireNonNull(environment.getProperty(Constants.FEIGN_READ_TIMEOUT)));
        return new Request.Options(connectionTimeout, TimeUnit.MILLISECONDS,
                readTimeout, TimeUnit.MILLISECONDS, true);
    }
}
