package com.ecommerce.ecommerce_be.controllers;


import com.ecommerce.ecommerce_be.services.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping(value = "products", method = RequestMethod.GET)
    public ResponseEntity<String> getAllProducts() {
        return this.productService.getProducts();
    }
}
