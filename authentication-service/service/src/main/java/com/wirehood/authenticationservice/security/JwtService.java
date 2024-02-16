package com.wirehood.authenticationservice.security;

import com.wirehood.authenticationservice.dto.UserDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

import static io.vavr.control.Try.of;
import static org.slf4j.LoggerFactory.getLogger;

@Component
public class JwtService {

    private static final Logger LOGGER = getLogger(JwtService.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    public String generateToken(UserDto userDto) {

        var claims = Jwts.claims().setSubject(userDto.getUsername());
        claims.put("roles", userDto.getRoles());

        return of(() ->
                Jwts.builder()
                        .setClaims(claims)
                        .setIssuedAt(new Date(System.currentTimeMillis()))
                        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1h
                        .signWith(SignatureAlgorithm.HS512, jwtSecret)
                        .compact()
        ).onFailure(t -> LOGGER.error("Error while generating token for user '{}'", userDto.getUsername(), t))
                .get();
    }
}
