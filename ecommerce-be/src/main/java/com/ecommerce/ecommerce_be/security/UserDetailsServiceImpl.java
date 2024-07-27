package com.ecommerce.ecommerce_be.security;

import com.ecommerce.ecommerce_be.entities.Users;
import com.ecommerce.ecommerce_be.repos.UsersRepo;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsersRepo usersRepo;

    public UserDetailsServiceImpl(UsersRepo usersRepo) {
        this.usersRepo = usersRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = this.usersRepo.findByLogin(username);
        if(user == null) {
            throw new UsernameNotFoundException("User can't be found: " + username);
        }
        return new User(user.getLogin(), user.getPassword(), Collections.emptyList());
    }
}
