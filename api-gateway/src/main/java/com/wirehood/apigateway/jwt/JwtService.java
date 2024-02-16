package com.wirehood.apigateway.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static io.vavr.control.Try.of;
import static org.slf4j.LoggerFactory.getLogger;

@Component
public class JwtService {

    private static final Logger LOGGER = getLogger(JwtService.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    public Try<Claims> getClaims(final String token) {
        return of(() -> Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody())
                .onFailure(t -> LOGGER.error("Error while getting token claims", t));
    }
}
