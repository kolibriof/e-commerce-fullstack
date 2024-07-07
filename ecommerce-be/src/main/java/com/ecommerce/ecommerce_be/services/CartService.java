package com.ecommerce.ecommerce_be.services;

import com.ecommerce.ecommerce_be.builders.ResponseBuilder;
import com.ecommerce.ecommerce_be.entities.Cart;
import com.ecommerce.ecommerce_be.entities.Product;
import com.ecommerce.ecommerce_be.entities.Users;
import com.ecommerce.ecommerce_be.repos.CartRepo;
import com.ecommerce.ecommerce_be.repos.ProductsRepo;
import com.ecommerce.ecommerce_be.repos.UsersRepo;
import com.ecommerce.ecommerce_be.requests.AddNewCartItemRequest;
import com.ecommerce.ecommerce_be.requests.CartContentsRequest;
import com.ecommerce.ecommerce_be.responses.ResultResponse;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.ecommerce.ecommerce_be.constants.ResponseConstants;

import java.util.List;

@Transactional
@Service
public class CartService {
    CartRepo cartRepo;
    UsersRepo usersRepo;
    ProductsRepo productsRepo;

    public CartService(CartRepo cartRepo, UsersRepo usersRepo, ProductsRepo productsRepo) {
        this.cartRepo = cartRepo;
        this.usersRepo = usersRepo;
        this.productsRepo = productsRepo;
    }

    public ResponseEntity<String> verifyCartContentsByRequest(CartContentsRequest request) {
        if(this.userExists(request)) {
            List<Cart> foundCartItems = this.cartRepo.findByUserID(request.getId());
            if(foundCartItems.isEmpty()) {
               return new ResponseEntity<>( this.cartResponse(ResponseConstants.NO_CART_ITEMS_FOUND, request.getUsername()), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(ResponseBuilder.buildResponse(foundCartItems), HttpStatus.OK);
        }
        return new ResponseEntity<>(this.cartResponse(ResponseConstants.USER_DOESNT_EXIST, request.getUsername()), HttpStatus.BAD_REQUEST);
    }


    public ResponseEntity<String> addCartProductByRequest(AddNewCartItemRequest request) {
        if(this.userExists(request)){
            Product foundProduct = this.productsRepo.findByName(request.getProduct_name());
            if(foundProduct == null) {
                return new ResponseEntity<>(this.cartResponse(ResponseConstants.NO_SUCH_ITEM_FOUND, request.getProduct_name()), HttpStatus.NOT_FOUND);
            }

            if(userHasTheProduct(request)) {
                return new ResponseEntity<>(this.cartResponse(ResponseConstants.PRODUCT_ALREADY_IN_CART, request.getProduct_name()), HttpStatus.BAD_REQUEST);
            }

            Users users = this.usersRepo.findByLogin(request.getUsername());

            Cart cartProduct = new Cart();

            cartProduct.setName(foundProduct.getName());
            cartProduct.setPrice(foundProduct.getPrice());
            cartProduct.setPerson(users);

            this.cartRepo.save(cartProduct);

            return new ResponseEntity<>(this.cartResponse(ResponseConstants.ITEM_ADDED_TO_CART, foundProduct.getName()), HttpStatus.OK);
        }
        return new ResponseEntity<>(this.cartResponse(ResponseConstants.USER_DOESNT_EXIST, request.getUsername()), HttpStatus.NOT_FOUND);
    }

    private Boolean userExists(CartContentsRequest request) {
        Users foundUserID = this.usersRepo.findByLogin(request.getUsername());
        if(request.getId() != null && foundUserID != null) {
            return request.getId().equals(foundUserID.getId());
        }
        return false;
    }

    private Boolean userHasTheProduct(AddNewCartItemRequest request) {
        Integer foundProduct = this.cartRepo.findByNameAndUserId(request.getProduct_name(), request.getId());
        return foundProduct != null;
    }

    private String cartResponse(String message, String cause) {
        return ResponseBuilder.buildResponse(new ResultResponse(message, cause));
    }
}
