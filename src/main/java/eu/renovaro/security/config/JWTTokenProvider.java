package eu.renovaro.security.config;

import eu.renovaro.common.exception.TaskApiException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JWTTokenProvider {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private Long jwtExpiration;
//TODO add refresh token logic, and logout logic
    public String generateToken(Authentication authentication){
        Date currentDate = new Date();
        Date expirationDate = new Date(currentDate.getTime() + jwtExpiration);

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        List<String> roles = authorities.stream()
                .map(Object::toString)
                .collect(Collectors.toList());

        String token = Jwts.builder()
                .subject(authentication.getName())
                .claim("roles", roles)
                .issuedAt(currentDate)
                .expiration(expirationDate)
                .signWith(key())
                .compact();
        return token;
    }

    public String getUsername(String token){
        Claims claims = Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    public List<String> getRolesFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.get("roles", List.class);
    }

    public boolean validateToken(String token) {
        try{
            Jwts.parser()
                    .verifyWith(key())
                    .build()
                    .parse(token);
            return true;
        }
        catch(MalformedJwtException ex){
            throw new TaskApiException("Invalid JWT token");
        }
        catch(ExpiredJwtException ex){
            throw new TaskApiException("Expired JWT token");
        }
        catch(UnsupportedJwtException ex){
            throw new TaskApiException("Unsupported JWT token");
        }
        catch(IllegalArgumentException ex){
            throw new TaskApiException("JWT claims string is empty");
        }
    }

    private SecretKey key() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
