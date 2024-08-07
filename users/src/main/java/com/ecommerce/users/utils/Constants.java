package com.ecommerce.users.utils;

public interface Constants {
    public static final String AUTH_ENDPOINT = "lb://authservice/auth/v1/";
    public static final String FEIGN_CONNECTION_TIMEOUT = "feign.connection.timeout";
    public static final String FEIGN_READ_TIMEOUT = "feign.read.timeout";
}
