package codeio.practise.utils;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
    private final String SECRET = "Hello MY name is Kavishri R. Iam currently studying in nmnjec in csbs, this is my frst cmplete backend+frontend project.. lets see to what extent i will be going and iam giving this long to fill the 120 bits criteria!!!";
    private final long EXPIRATION = 1000 * 60 * 60;
    private final Key secretKey = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    public String generateToken(String email){
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String extractEmail(String token){
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

    }

    public boolean validateToken(String token) {
        try {
            extractEmail(token);
            return true;
        } catch (JwtException e) {
            // Could log e.getMessage() for debug purposes
            return false;
        }
    }
}
