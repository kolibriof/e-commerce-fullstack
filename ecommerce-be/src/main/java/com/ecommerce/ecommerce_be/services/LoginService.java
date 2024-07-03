package com.ecommerce.ecommerce_be.services;


import com.ecommerce.ecommerce_be.entities.Users;
import com.ecommerce.ecommerce_be.repos.UsersRepo;
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
        Integer foundUser = this.usersRepo.findByLogin(users.getLogin());

        if(foundUser != null) {
            return new ResponseEntity<>(
                    buildResponse(new UsersResponse("The user already exists", users.getLogin())),
                    HttpStatus.BAD_REQUEST);
        }

        this.usersRepo.save(users);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
