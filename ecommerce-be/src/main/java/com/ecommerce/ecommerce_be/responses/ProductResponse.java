package com.ecommerce.ecommerce_be.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ProductResponse {
    private String name;
    private String description;
    private String imglink;
}
