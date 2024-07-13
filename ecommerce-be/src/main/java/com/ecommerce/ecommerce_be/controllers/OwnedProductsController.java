package com.ecommerce.ecommerce_be.controllers;

import com.ecommerce.ecommerce_be.requests.CartContentsRequest;
import com.ecommerce.ecommerce_be.services.OwnedProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OwnedProductsController {
    OwnedProductService ownedProductService;

    public OwnedProductsController(OwnedProductService ownedProductService) {
        this.ownedProductService = ownedProductService;
    }

    @RequestMapping(value = "/owned", method = RequestMethod.POST)
    ResponseEntity<String> getOwnedProducts(@RequestBody CartContentsRequest request) {
        return this.ownedProductService.getOwnedProductPerRequest(request);
    }
}
