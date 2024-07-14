package com.ecommerce.ecommerce_be.services;

import com.ecommerce.ecommerce_be.builders.ResponseBuilder;
import com.ecommerce.ecommerce_be.constants.ResponseConstants;
import com.ecommerce.ecommerce_be.entities.OwnedProducts;
import com.ecommerce.ecommerce_be.entities.Users;
import com.ecommerce.ecommerce_be.repos.OwnedProductsRepo;
import com.ecommerce.ecommerce_be.repos.UsersRepo;
import com.ecommerce.ecommerce_be.requests.CartContentsRequest;
import com.ecommerce.ecommerce_be.responses.ResultResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    @Transactional
    public ResponseEntity<String> sellOwnedProductsPerIds(Integer productId, Integer userId) {
        Float currentUserBalance;
        OwnedProducts foundOwnedProduct = this.ownedProductsRepo.findSingleProductByUserAndProductID(productId, userId);
        Optional<Users> foundUserById = this.usersRepo.findById(userId);

        if(foundUserById.isPresent()) {
             currentUserBalance = this.usersRepo.getUserBalance(
                    foundUserById.get().getLogin()
            );
        } else {
            return new ResponseEntity<>(ResponseBuilder.buildResponse(new ResultResponse("User with such ID cannot be found.", userId.toString())), HttpStatus.BAD_REQUEST);
        }

        if(foundOwnedProduct == null) {
            return new ResponseEntity<>(ResponseBuilder.buildResponse(new ResultResponse("User does not own this product.", productId.toString())), HttpStatus.BAD_REQUEST);
        }

        this.ownedProductsRepo.deleteById(foundOwnedProduct.getId());

        this.usersRepo.updateUserBalance(currentUserBalance + foundOwnedProduct.getProduct().getPrice(), userId);

        return new ResponseEntity<>(ResponseBuilder.buildResponse(new ResultResponse("Product has been sold successfully.", foundOwnedProduct.getProduct().getName())), HttpStatus.OK);
    }


    private Boolean userExists(CartContentsRequest request) {
        Users foundUser = this.usersRepo.findByLogin(request.getUsername());
        return foundUser.getId().equals(request.getId());
    }
}
