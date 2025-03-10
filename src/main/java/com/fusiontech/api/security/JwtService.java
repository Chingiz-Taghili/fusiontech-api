package com.fusiontech.api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

//Jwt token-lə əlaqəli metodların toplandığı servis
@Component
public class JwtService {

    //Dəyişənlərin dəyəri application.properties-dən götürülür
    @Value("${jwt.signing.key}")
    private String secretKey;
    @Value("${jwt.token.expiration}")
    private long tokenExpiration;

    //Token-in içindən username-i çıxardır
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    //Token-in içindən son istifadə müddətini çıxardır
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    //Token-in içindən hər hansı claim-i (məlumatı) çıxardır
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    //Bütün claim-ləri çıxardır
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
    }

    //Token-in istifadə müddətini yoxlayır
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    //Token-in doğruluğunu yoxlayır
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    //Username-ə uyğun token verir
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    //Yeni token düzəldir
    private String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder().setClaims(claims).setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    //SECRET_KEY-i decode edib qaytarır
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
