package com.cybertek.util;

import com.cybertek.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JWTUtil {

    @Value("${security.jwt.secret-key}")
    private String secret = "cybertek";

    public String generateToken(User user){

        Map<String, Object> claims = new HashMap<>();
        claims.put("username", user.getUserName());
        claims.put("id", user.getId());
        claims.put("firstName", user.getFirstName());
        claims.put("lastName", user.getLastName());
        return createToken(claims, user.getUserName());
    }

    private String createToken(Map<String,Object> claims, String username){  // this method creates token

        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // ten hours valid token
                .signWith(SignatureAlgorithm.HS256,secret)
                .compact();
    }

    private Claims extractAllClaims(String token){ // this method decodes token

        return Jwts
                .parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver){  // this method convert Claims to type T..why this is generic because we gonna extrack more than one type -> username, expirationDate vs.
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject); // method referance ile function girdik....username extract ettik...
    }

    public Date extractExpiration(String token){
        return extractClaim(token,Claims::getExpiration); // method referance ile function girdik....expiration date extract ettik...

    }

    private Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails){
        final String currentUser = extractAllClaims(token).get("id").toString(); // this line extracts username with the id -> createToken(claims, user.getUserName());
        return (currentUser.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
