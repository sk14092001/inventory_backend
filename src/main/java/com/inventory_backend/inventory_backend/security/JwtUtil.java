//package com.inventory_backend.inventory_backend.security;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//
//import javax.crypto.SecretKey;
//import java.nio.charset.StandardCharsets;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//@Component
//public class JwtUtil {
//
//    // ✅ MUST be at least 32 characters (256 bits)
//    @Value("${jwt.secret}")
//    private String secret;
//
//    @Value("${jwt.expiration}")
//    private long expiration;
//
//    private SecretKey getSigningKey() {
//        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
//    }
//
//    // 🔑 Generate JWT
//    public String generateToken(UserDetails userDetails) {
//
//        Map<String, Object> claims = new HashMap<>();
//        claims.put(
//                "roles",
//                userDetails.getAuthorities().stream()
//                        .map(GrantedAuthority::getAuthority)
//                        .toList()
//        );
//
//        return Jwts.builder()
//                .setClaims(claims)
//                .setSubject(userDetails.getUsername()) // email
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + expiration))
//                .signWith(getSigningKey())
//                .compact();
//    }
//
//    // 📧 Extract email
//    public String extractEmail(String token) {
//        return extractClaims(token).getSubject();
//    }
//
//    // ✅ Validate token
//    public boolean isTokenValid(String token, UserDetails userDetails) {
//        return extractEmail(token).equals(userDetails.getUsername())
//                && !extractClaims(token).getExpiration().before(new Date());
//    }
//
//    // 🔍 Parse claims
//    private Claims extractClaims(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(getSigningKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//}