package com.ecommerce.ecommerce_be.repos;

import com.ecommerce.ecommerce_be.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartRepo extends JpaRepository<Cart, Integer> {

    @Query("SELECT c FROM Cart c JOIN Users u ON c.person.id = :userid")
    List<Cart> findByUserID(@Param("userid") int id);

    @Query("SELECT c FROM Cart c WHERE c.name = :name AND c.person.id = :id")
    List<Cart> findByNameAndUserId(@Param("name") String name, @Param("id") Integer id);

    @Query("SELECT c.id FROM Cart c WHERE c.name = :name AND c.person.id = :id")
    Integer findByNameAndUserId_returnID(@Param("name") String name, @Param("id") Integer id);
}
