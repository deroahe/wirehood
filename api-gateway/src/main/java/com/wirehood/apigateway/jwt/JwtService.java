package com.wirehood.apigateway.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    public Claims getClaims(final String token) {
        return Try.of(() -> Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody())
                .getOrElseThrow(() -> new RuntimeException("Invalid token"));
    }

    public Try<Void> validateToken(final String token) {
            return Try.run(() -> Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token));
    }
}
