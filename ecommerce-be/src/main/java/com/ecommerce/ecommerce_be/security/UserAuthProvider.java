package com.ecommerce.ecommerce_be.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ecommerce.ecommerce_be.entities.Users;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class UserAuthProvider {
    @Value("${security.jwt.token.secret-key:secret-key}")
    private String secretKey;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(Users users) {
        Date now = new Date();
        //TODO: change to a valid time after testing
        Date validity = new Date(now.getTime() + 3600000);
        return JWT
                .create()
                .withIssuer(users.getLogin())
                .withIssuedAt(Instant.now())
                .withExpiresAt(validity)
                .withClaim("login", users.getLogin())
                .sign(Algorithm.HMAC256(secretKey));
    }

    public Authentication validateToken(String token) throws AccessDeniedException {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);

            String login = decodedJWT.getClaim("login").asString();

            return new UsernamePasswordAuthenticationToken(login, null, Collections.emptyList());
        } catch (Exception e) {
            System.out.println("Invalid token: " + e.getMessage());
            throw new AccessDeniedException(e.getMessage());
        }
    }

}
