package com.ecommerce.ecommerce_be.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@Entity()
@Table(name = "spring_session", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Session {

    @Id
    @GeneratedValue(generator = "id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "id_seq", allocationSize = 1)
    @Column(name = "primary_id")
    String id;

    @Column(name="session_id")
    String sessionId;

    @Column(name = "creation_time")
    BigInteger creationTime;

    @Column(name = "last_access_time")
    BigInteger lastAccessTime;

    @Column(name = "max_inactive_interval")
    Integer maxInactiveTime;

    @Column(name="expiry_time")
    BigInteger expiryTime;

    @Column(name = "principal_name")
    String principalName;
}
