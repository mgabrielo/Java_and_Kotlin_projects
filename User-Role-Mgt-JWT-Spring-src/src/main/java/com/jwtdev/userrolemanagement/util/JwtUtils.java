package com.jwtdev.userrolemanagement.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtils {

    private static  final String jwtSigningKey = "secret";

    private static  final  int TOKEN_VALID = 3600 * 5;
    public  String getUsernameFromToken(String token){
        return getClaimFromToken(token, Claims::getSubject);
    }


    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token){
       return Jwts.parser().setSigningKey(jwtSigningKey).parseClaimsJws(token).getBody();
    }


    public Boolean validateToken(String token, UserDetails userDetails){
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private Boolean isTokenExpired(String token){
        return getExpirationDateFromToken(token).before(new Date());
    }


    public Date getExpirationDateFromToken(String token){
        return getClaimFromToken( token, Claims::getExpiration);
    }


    public  String generateToken(UserDetails userDetails){
        Map<String,Object> claims = new HashMap<>();
        return Jwts.builder().setClaims(claims).setSubject(userDetails.getUsername())
                .claim("authorities", userDetails.getAuthorities())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALID * 1000))
                .signWith(SignatureAlgorithm.HS256, jwtSigningKey).compact();
    }
}
