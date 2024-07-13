package com.ecommerce.ecommerce_be.services;

import com.ecommerce.ecommerce_be.builders.ResponseBuilder;
import com.ecommerce.ecommerce_be.constants.ResponseConstants;
import com.ecommerce.ecommerce_be.entities.OwnedProducts;
import com.ecommerce.ecommerce_be.entities.Users;
import com.ecommerce.ecommerce_be.repos.OwnedProductsRepo;
import com.ecommerce.ecommerce_be.repos.UsersRepo;
import com.ecommerce.ecommerce_be.requests.CartContentsRequest;
import com.ecommerce.ecommerce_be.responses.ResultResponse;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OwnedProductService {

    OwnedProductsRepo ownedProductsRepo;
    UsersRepo usersRepo;

    public OwnedProductService(OwnedProductsRepo ownedProductsRepo, UsersRepo usersRepo) {
        this.ownedProductsRepo = ownedProductsRepo;
        this.usersRepo = usersRepo;
    }

    @Transactional
    public ResponseEntity<String> getOwnedProductPerRequest(CartContentsRequest request) {
        if(!userExists(request)) {
            return new ResponseEntity<>(ResponseBuilder.buildResponse(new ResultResponse(ResponseConstants.USER_DOESNT_EXIST, request.getUsername())), HttpStatus.BAD_REQUEST);
        }
        List<OwnedProducts> foundOwnedProducts = this.ownedProductsRepo.findOwnedProductsByUserID(request.getId());

        if(foundOwnedProducts.isEmpty()) {
            return new ResponseEntity<>(ResponseBuilder.buildResponse(new ResultResponse("User does not own any products.", request.getUsername())), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(ResponseBuilder.buildResponse(foundOwnedProducts), HttpStatus.OK);
    }


    private Boolean userExists(CartContentsRequest request) {
        Users foundUser = this.usersRepo.findByLogin(request.getUsername());
        return foundUser.getId().equals(request.getId());
    }
}
