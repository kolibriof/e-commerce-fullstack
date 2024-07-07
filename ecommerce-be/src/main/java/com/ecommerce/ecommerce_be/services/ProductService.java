package com.ecommerce.ecommerce_be.services;

import com.ecommerce.ecommerce_be.entities.Product;
import com.ecommerce.ecommerce_be.repos.ProductsRepo;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.ecommerce.ecommerce_be.builders.ResponseBuilder;

import java.util.List;

@Service
public class ProductService {

    ProductsRepo productsRepo;

    public ProductService(ProductsRepo productsRepo) {
        this.productsRepo = productsRepo;
    }

    @Transactional
    public ResponseEntity<String> getProducts() {
        List<Product> products = productsRepo.findAll();
        if(!products.isEmpty()) {
            return new ResponseEntity<>(ResponseBuilder.buildResponse(products), HttpStatus.OK);
        }
        return new ResponseEntity<>("no products found.", HttpStatus.NOT_FOUND);
    }
}
