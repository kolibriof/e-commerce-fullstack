package com.ecommerce.ecommerce_be.repos;

import com.ecommerce.ecommerce_be.entities.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepo extends JpaRepository<Session, String> {

    @Query("SELECT s FROM Session s WHERE s.principalName = :name")
    List<Session> findByPrincipalName(@Param("name") String name);
}
