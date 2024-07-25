package com.ecommerce.gateway.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenResource {
    String id;
    String email;
    Long expiry;
    Long issuedAt;
}
