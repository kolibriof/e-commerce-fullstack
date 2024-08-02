package com.ecommerce.ecommerce_be.services;


import com.ecommerce.ecommerce_be.builders.ResponseBuilder;
import com.ecommerce.ecommerce_be.entities.Users;
import com.ecommerce.ecommerce_be.repos.UsersRepo;
import com.ecommerce.ecommerce_be.responses.ResultResponse;
import com.ecommerce.ecommerce_be.responses.UsersResponse;
import com.ecommerce.ecommerce_be.security.UserAuthProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.ecommerce.ecommerce_be.builders.ResponseBuilder.buildResponse;

@Service
public class LoginService {


    UsersRepo usersRepo;
    UserAuthProvider userAuthProvider;

    public LoginService(UsersRepo usersRepo, UserAuthProvider userAuthProvider) {
        this.userAuthProvider = userAuthProvider;
        this.usersRepo = usersRepo;
    }

    @Transactional
    public ResponseEntity<String> authenticateUser(Users users, HttpServletRequest request) {
        Integer foundUser = this.usersRepo.findByLoginAndPassword(users.getLogin(), users.getPassword());

        if (foundUser == null) {
            return new ResponseEntity<>("Authentication has failed.", HttpStatus.BAD_REQUEST);
        }

        String token = this.userAuthProvider.createToken(users);

        System.out.println("NEW TOKEN GENERATED: " + token);

        return new ResponseEntity<>(buildResponse(new UsersResponse(foundUser.toString(),
                token)),
                HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<String> createNewUser(Users users, HttpServletRequest request) {
        Users foundUser = this.usersRepo.findByLogin(users.getLogin());

        if (foundUser != null) {
            return new ResponseEntity<>(
                    buildResponse(new UsersResponse("The user already exists", users.getLogin())),
                    HttpStatus.BAD_REQUEST);
        }


        Users newUser = this.usersRepo.save(users);
        return new ResponseEntity<>(buildResponse(new UsersResponse(newUser.getId().toString(),
                this.userAuthProvider.createToken(users))),
                HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<String> getUserBalance(Users users) {
        Users foundUser = this.usersRepo.findByLogin(users.getLogin());

        if (foundUser == null) {
            return new ResponseEntity<>(ResponseBuilder.buildResponse(new ResultResponse("User not found.",
                    users.getLogin())), HttpStatus.BAD_REQUEST);
        }

        Float balance = this.usersRepo.getUserBalance(users.getLogin());

        if (balance == null) {
            return new ResponseEntity<>(ResponseBuilder.buildResponse(new ResultResponse("Balance cannot be retrieved.",
                    users.getLogin())), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(ResponseBuilder.buildResponse(balance), HttpStatus.OK);

    }

    @Transactional
    public ResponseEntity<String> logoutUser(HttpSession session) {
        System.out.println("SESSION HAS BEEN INVALIDATED WITH ID:" + session.getId());
        session.invalidate();
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
