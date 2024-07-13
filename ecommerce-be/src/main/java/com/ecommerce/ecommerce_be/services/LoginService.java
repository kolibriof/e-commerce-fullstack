package com.ecommerce.ecommerce_be.services;


import com.ecommerce.ecommerce_be.builders.ResponseBuilder;
import com.ecommerce.ecommerce_be.entities.Users;
import com.ecommerce.ecommerce_be.repos.UsersRepo;
import com.ecommerce.ecommerce_be.responses.ResultResponse;
import com.ecommerce.ecommerce_be.responses.UsersResponse;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.ecommerce.ecommerce_be.builders.ResponseBuilder.buildResponse;

@Service
public class LoginService {

    UsersRepo usersRepo;

    public LoginService(UsersRepo usersRepo) {
        this.usersRepo = usersRepo;
    }

    @Transactional
    public ResponseEntity<String> authenticateUser(String login, String password) {
        Integer foundUser = this.usersRepo.findByLoginAndPassword(login, password);
        if(foundUser == null) {
            return new ResponseEntity<>("Authentication has failed.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(foundUser.toString(), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<String> createNewUser(Users users) {
        Users foundUser = this.usersRepo.findByLogin(users.getLogin());

        if(foundUser != null) {
            return new ResponseEntity<>(
                    buildResponse(new UsersResponse("The user already exists", users.getLogin())),
                    HttpStatus.BAD_REQUEST);
        }

        this.usersRepo.save(users);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<String> getUserBalance(Users users) {
        Users foundUser = this.usersRepo.findByLogin(users.getLogin());

        if(foundUser == null) {
        return new ResponseEntity<>(ResponseBuilder.buildResponse(new ResultResponse("User not found.", users.getLogin())), HttpStatus.BAD_REQUEST);
        }

        Float balance = this.usersRepo.getUserBalance(users.getLogin());

        if(balance == null) {
            return new ResponseEntity<>(ResponseBuilder.buildResponse(new ResultResponse("Balance cannot be retrieved.", users.getLogin())), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(ResponseBuilder.buildResponse(balance), HttpStatus.OK);

    }
}
