package com.ecommerce.ecommerce_be.repos;

import com.ecommerce.ecommerce_be.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UsersRepo extends JpaRepository<Users, Integer> {
    @Query("SELECT u.id FROM Users u WHERE u.login = :login AND u.password = :password")
    Integer findByLoginAndPassword(@Param("login") String login, @Param("password") String password);

    @Query("SELECT u FROM Users u WHERE u.login = :login")
    Users findByLogin(@Param("login") String login);

}
