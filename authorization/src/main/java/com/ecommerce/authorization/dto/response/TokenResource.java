package com.ecommerce.authorization.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class TokenResource {
    String id;
    String email;
    Long expiry;
    Long issuedAt;
}
