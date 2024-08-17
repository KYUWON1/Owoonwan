package com.example.owoonwan.jwt;

import com.example.owoonwan.type.UserRole;
import io.jsonwebtoken.Jwts;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {
    private SecretKey secretKey;

    public JwtUtil (@Value("${jwt.secret}") String secretKey){
        byte[] decodedKey = Base64.getDecoder().decode(secretKey);
        this.secretKey =
                new SecretKeySpec(decodedKey,
                        "HmacSHA256");
    }

    public String getUserId(String token){
        return Jwts.parser().setSigningKey(secretKey)
                .build().parseSignedClaims(token).getPayload()
                .get("userId",String.class);
    }

    public String getRole(String token){
        String role = Jwts.parser().setSigningKey(secretKey)
                .build().parseSignedClaims(token).getPayload()
                .get("role",String.class);
        return role;
    }

    public boolean isExpiredToken(String token){
        return Jwts.parser().setSigningKey(secretKey)
                .build().parseSignedClaims(token).getPayload()
                .getExpiration().before(new Date());
    }

    public String createJwtToken(String userId,String role,Long expiredMs){
        return Jwts.builder()
                .claim("userId",userId)
                .claim("role",role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }
}
