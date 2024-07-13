package com.ecommerce.ecommerce_be.repos;

import com.ecommerce.ecommerce_be.entities.OwnedProducts;
import com.ecommerce.ecommerce_be.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OwnedProductsRepo extends JpaRepository<OwnedProducts, Integer> {

    @Query("SELECT o.product FROM OwnedProducts o WHERE o.users.id = :userid AND o.product.id = :productid")
    Product findOwnedUserProduct(@Param("userid") Integer userid, @Param("productid") Integer productid);

    @Query("SELECT o FROM OwnedProducts o INNER JOIN Product p ON p.id = o.users.id WHERE o.users.id = :id")
    List<OwnedProducts> findOwnedProductsByUserID(@Param("id") Integer id);
}
