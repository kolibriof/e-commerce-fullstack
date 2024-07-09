package com.ecommerce.ecommerce_be.controllers;

import com.ecommerce.ecommerce_be.requests.AddNewCartItemRequest;
import com.ecommerce.ecommerce_be.requests.CartContentsRequest;
import com.ecommerce.ecommerce_be.services.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CartController {
    CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @RequestMapping(value = "cart", method = RequestMethod.POST)
    public ResponseEntity<String> getCartProducts(@RequestBody CartContentsRequest request) {
        return this.cartService.verifyCartContentsByRequest(request);
    }

    @RequestMapping(value = "cartproduct", method = RequestMethod.POST)
    public ResponseEntity<String> putProductInCart(@RequestBody AddNewCartItemRequest request) {
        return this.cartService.addCartProductByRequest(request);
    }

    @RequestMapping(value = "removeproduct", method = RequestMethod.POST)
    public ResponseEntity<String> removeProductFromCart(@RequestBody AddNewCartItemRequest request) {
        return this.cartService.removeProductByRequest(request);
    }

}
