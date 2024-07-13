package com.ecommerce.ecommerce_be.controllers;

import com.ecommerce.ecommerce_be.entities.Users;
import com.ecommerce.ecommerce_be.services.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    LoginService loginService;

    public UserController(LoginService loginService) {
        this.loginService = loginService;
    }

    @RequestMapping(value="/users", method = RequestMethod.POST)
    public ResponseEntity<String> createUser(@RequestBody Users users, @RequestParam(required = false, defaultValue = "false") Boolean balance) {
        if(balance) {
            return this.loginService.getUserBalance(users);
        }
        return loginService.createNewUser(users);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<String> loginUser(@RequestBody Users users) {
        return loginService.authenticateUser(users.getLogin(), users.getPassword());
    }
}
