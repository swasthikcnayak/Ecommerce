package com.ecommerce.authorization.dto.response;

import com.ecommerce.authorization.dto.Role;

import lombok.Data;

@Data
public class UserRequest {
    String userId;
    Role role;
}
