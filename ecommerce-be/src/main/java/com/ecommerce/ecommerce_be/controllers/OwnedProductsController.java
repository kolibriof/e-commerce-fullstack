package com.ecommerce.ecommerce_be.controllers;

import com.ecommerce.ecommerce_be.requests.CartContentsRequest;
import com.ecommerce.ecommerce_be.services.OwnedProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class OwnedProductsController {
    OwnedProductService ownedProductService;

    public OwnedProductsController(OwnedProductService ownedProductService) {
        this.ownedProductService = ownedProductService;
    }

    @RequestMapping(value = "/owned", method = RequestMethod.POST)
    ResponseEntity<String> getOwnedProducts(@RequestBody CartContentsRequest request,
                                            @RequestParam(required = false, defaultValue = "") Integer productId,
                                            @RequestParam(required = false, defaultValue = "") Integer userId) {
        if(productId != null && userId != null) {
            return this.ownedProductService.sellOwnedProductsPerIds(productId, userId);
        }
        return this.ownedProductService.getOwnedProductPerRequest(request);
    }
}
