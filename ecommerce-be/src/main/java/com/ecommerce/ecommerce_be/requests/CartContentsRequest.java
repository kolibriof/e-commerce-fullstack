package com.ecommerce.ecommerce_be.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartContentsRequest {
    private Integer id;
    private String username;
}
