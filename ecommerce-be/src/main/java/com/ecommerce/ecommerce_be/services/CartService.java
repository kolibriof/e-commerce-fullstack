package com.ecommerce.ecommerce_be.services;

import com.ecommerce.ecommerce_be.builders.ResponseBuilder;
import com.ecommerce.ecommerce_be.entities.Cart;
import com.ecommerce.ecommerce_be.entities.OwnedProducts;
import com.ecommerce.ecommerce_be.entities.Product;
import com.ecommerce.ecommerce_be.entities.Users;
import com.ecommerce.ecommerce_be.repos.CartRepo;
import com.ecommerce.ecommerce_be.repos.OwnedProductsRepo;
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
import java.util.Optional;

@Transactional
@Service
public class CartService {
    CartRepo cartRepo;
    UsersRepo usersRepo;
    ProductsRepo productsRepo;
    OwnedProductsRepo ownedProductsRepo;

    public CartService(CartRepo cartRepo, UsersRepo usersRepo, ProductsRepo productsRepo, OwnedProductsRepo ownedProductsRepo) {
        this.ownedProductsRepo = ownedProductsRepo;
        this.cartRepo = cartRepo;
        this.usersRepo = usersRepo;
        this.productsRepo = productsRepo;
    }

    public ResponseEntity<String> verifyCartContentsByRequest(CartContentsRequest request) {
        if(this.userExists(request)) {
            List<Cart> foundCartItems = this.cartRepo.findByUserID(request.getId());
            if(foundCartItems.isEmpty()) {
               return new ResponseEntity<>( this.cartResponse(ResponseConstants.NO_CART_ITEMS_FOUND, request.getUsername()), HttpStatus.OK);
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

    public ResponseEntity<String> removeProductByRequest(AddNewCartItemRequest request) {
        if(userExists(request)) {
            Product foundProduct = this.productsRepo.findByName(request.getProduct_name());

            if(foundProduct == null) {
                return new ResponseEntity<>(this.cartResponse(ResponseConstants.NO_SUCH_ITEM_FOUND, request.getProduct_name()), HttpStatus.BAD_REQUEST);
            }

            if(!userHasTheProduct(request)) {
                return new ResponseEntity<>(this.cartResponse(ResponseConstants.USER_DOESNT_HAVE_THE_PRODUCT, request.getProduct_name()), HttpStatus.BAD_REQUEST);
            }

            List<Cart> foundCartProduct = this.cartRepo.findByNameAndUserId(request.getProduct_name(), request.getId());

            if(foundCartProduct.isEmpty()) {
                return new ResponseEntity<>(this.cartResponse(ResponseConstants.PRODUCT_NOT_FOUND_IN_CART, request.getProduct_name()), HttpStatus.BAD_REQUEST);
            }

            Optional<Cart> desiredProductToDelete = foundCartProduct.stream().filter((item)-> item.getName().equals(request.getProduct_name())).findFirst();

            if(desiredProductToDelete.isPresent()) {
               Cart product = desiredProductToDelete.get();

               this.cartRepo.deleteById(product.getId());
               return new ResponseEntity<>(this.cartResponse(ResponseConstants.ITEM_HAS_BEEN_DELETED_CART, request.getProduct_name()), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(this.cartResponse("Unexpected error.", request.getProduct_name()), HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>(this.cartResponse(ResponseConstants.USER_DOESNT_EXIST, request.getUsername()), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> payForTheCartProducts(CartContentsRequest request, Float sum) {
        float newUserBalance;

        if(!userExists(request)) {
            return new ResponseEntity<>(this.cartResponse(ResponseConstants.USER_DOESNT_EXIST, request.getUsername()), HttpStatus.BAD_REQUEST);
        }

        Float userBalance = this.usersRepo.getUserBalance(request.getUsername());


        if(!(userBalance >= sum)) {
          return new ResponseEntity<>(this.cartResponse("Not enough money.", userBalance.toString()), HttpStatus.BAD_REQUEST);
        }


        newUserBalance = userBalance - sum;

        List<Cart> userProducts = this.cartRepo.findByUserID(request.getId());
        Users productOwner = this.usersRepo.findByLogin(request.getUsername());


        for(Cart product : userProducts) {
            Product foundProduct = this.productsRepo.findByName(product.getName());
            OwnedProducts ownedProducts = new OwnedProducts();

            if(this.userOwnThisProduct(product) != null) {
                return new ResponseEntity<>(this.cartResponse("You already own this product", product.getName()), HttpStatus.BAD_REQUEST);
            }

            if(foundProduct != null) {
                ownedProducts.setProduct(foundProduct);
                ownedProducts.setUsers(productOwner);
                this.ownedProductsRepo.save(ownedProducts);
            } else {
                System.out.println("The product cannot be found.");
            }
        }

        this.clearCartByRequest(userProducts);

        this.usersRepo.updateUserBalance(newUserBalance, productOwner.getId());

        return new ResponseEntity<>(this.cartResponse("The operation was successful.", productOwner.getLogin()), HttpStatus.OK);
    }


    private void clearCartByRequest(List<Cart> cartList) {
        for(Cart item : cartList) {
            this.cartRepo.deleteById(item.getId());
        }
    }

    private Product userOwnThisProduct(Cart cartItem) {
        Product foundProduct = this.productsRepo.findByName(cartItem.getName());
        return this.ownedProductsRepo.findOwnedUserProduct(cartItem.getPerson().getId(), foundProduct.getId());
    }

    private Boolean userExists(CartContentsRequest request) {
        Users foundUserID = this.usersRepo.findByLogin(request.getUsername());
        if(request.getId() != null && foundUserID != null) {
            return request.getId().equals(foundUserID.getId());
        }
        return false;
    }


    private Boolean userHasTheProduct(AddNewCartItemRequest request) {
        Integer foundProduct = this.cartRepo.findByNameAndUserId_returnID(request.getProduct_name(), request.getId());
        return foundProduct != null;
    }

    private String cartResponse(String message, String cause) {
        return ResponseBuilder.buildResponse(new ResultResponse(message, cause));
    }
}
