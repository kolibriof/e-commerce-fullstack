package com.ecommerce.ecommerce_be.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class UsersResponse {
    private String message;
    private String errorCause;
}
