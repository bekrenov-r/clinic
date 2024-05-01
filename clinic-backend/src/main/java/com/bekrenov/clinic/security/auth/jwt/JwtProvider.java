package com.bekrenov.clinic.security.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtProvider {
    @Value("${spring.jwt.secret}")
    private String secret;

    @Value("${spring.jwt.expiration-time-millis}")
    private Long expirationTime;

    public String generateToken(UserDetails userDetails, String firstName) {
        var claims = createClaims(userDetails, firstName);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(this.getKey(), SignatureAlgorithm.HS256).compact();
    }

    public Boolean validateToken(String token) {
        Jwts.parserBuilder()
                .setSigningKey(this.getKey())
                .build()
                .parseClaimsJws(token);
        return !this.isTokenExpired(token);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = this.getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public String getSubject(String token) {
        return this.getAllClaimsFromToken(token).getSubject();
    }

    public Date getExpirationDate(String token) {
        return this.getAllClaimsFromToken(token).getExpiration();
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(this.getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        Date expiration = this.getExpirationDate(token);
        return expiration.before(new Date());
    }

    private Map<String, Object> createClaims(UserDetails userDetails, String firstName){
        List<String> rolesStr = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return Map.of(
                "roles", String.join(",", rolesStr),
                "fname", firstName
        );
    }

    private Key getKey(){
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}
