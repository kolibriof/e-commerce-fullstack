package com.ecommerce.ecommerce_be.requests;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddNewCartItemRequest extends CartContentsRequest{
    private String product_name;
}
