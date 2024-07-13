package com.ecommerce.ecommerce_be.controllers;

import com.ecommerce.ecommerce_be.requests.AddNewCartItemRequest;
import com.ecommerce.ecommerce_be.requests.CartContentsRequest;
import com.ecommerce.ecommerce_be.services.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CartController {
    CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @RequestMapping(value = "cart", method = RequestMethod.POST)
    public ResponseEntity<String> getCartProducts(@RequestBody CartContentsRequest request, @RequestParam(required = false, defaultValue = "") Float sum) {
        if(sum != null) {
            return this.cartService.payForTheCartProducts(request, sum);
        }
        return this.cartService.verifyCartContentsByRequest(request);
    }

    @RequestMapping(value = "cartproduct", method = RequestMethod.POST)
    public ResponseEntity<String> productManipulation(@RequestParam(required = false, defaultValue = "false") Boolean remove,
                                                   @RequestBody AddNewCartItemRequest request)
    {
        if(remove) {
            return this.cartService.removeProductByRequest(request);
        }
        return this.cartService.addCartProductByRequest(request);
    }

}
