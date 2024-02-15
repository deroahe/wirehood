package com.wirehood.authenticationservice.jwt;

import com.wirehood.authenticationservice.dto.UserDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    public String generateToken(UserDto userDto) {

        return Jwts.builder()
                .setSubject(userDto.getUsername())
                .setClaims(Map.of("roles", List.of(userDto.getRoles())))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }
}
