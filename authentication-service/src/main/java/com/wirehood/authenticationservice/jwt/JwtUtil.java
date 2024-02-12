package com.wirehood.authenticationservice.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;

import static io.jsonwebtoken.Claims.SUBJECT;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.token.validity}")
    private long tokenValidity;

    public Claims getClaims(final String token) {
        try {
            Claims body = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
            return body;
        } catch (Exception e) {
            System.out.println(e.getMessage() + " => " + e);
        }
        return null;
    }

    public String generateToken(String username) {
        var roleList = new ArrayList<>();
        roleList.add("admin");
        roleList.add("user");
        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + tokenValidity;
        Date exp = new Date(expMillis);
        return Jwts.builder().claim(SUBJECT, username).claim("roles", roleList).setIssuedAt(new Date(nowMillis)).setExpiration(exp)
                .signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
    }

    public void validateToken(final String token) /*throws JwtTokenMalformedException, JwtTokenMissingException*/ {
//        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
//        } catch (SignatureException ex) {
//            throw new JwtTokenMalformedException("Invalid JWT signature");
//        } catch (MalformedJwtException ex) {
//            throw new JwtTokenMalformedException("Invalid JWT token");
//        } catch (ExpiredJwtException ex) {
//            throw new JwtTokenMalformedException("Expired JWT token");
//        } catch (UnsupportedJwtException ex) {
//            throw new JwtTokenMalformedException("Unsupported JWT token");
//        } catch (IllegalArgumentException ex) {
//            throw new JwtTokenMissingException("JWT claims string is empty.");
//        }
    }
}
