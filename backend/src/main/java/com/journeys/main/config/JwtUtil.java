/**
 * @team Journeys
 * @file JwtUtil.java
 * @date January 21st, 2022
 */

package com.journeys.main.config;

import com.journeys.main.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Utility class to manipulate jwt token
 */
@Service
public class JwtUtil {

    @Autowired
    JwtProperties properties;

    /**
     * Method extracting the username
     * @param token a token
     * @return
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Method extracting the expiration date
     * @param token a token
     * @return the date of expiration
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     *
     * @param token
     * @param claimsResolver
     * @param <T>
     * @return
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     *
     * @param token
     * @return
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(properties.getSecretKey()).parseClaimsJws(token).getBody();
    }

    /**
     *
     * @param token
     * @return
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Method generating a token
     * @param userDetails a DTO of User
     * @return the token
     */
    public String generateToken(User userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUserName());
    }

    /**
     *
     * @param claims
     * @param subject
     * @return
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * Integer.parseInt(properties.getDuration())))
                .signWith(SignatureAlgorithm.HS256, properties.getSecretKey()).compact();
    }

    /**
     *
     * @param token
     * @param userDetails a DTO of User
     * @return true if valid, else false
     */
    public Boolean validateToken(String token, User userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUserName()) && !isTokenExpired(token));
    }
}