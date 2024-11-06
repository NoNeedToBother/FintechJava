package ru.kpfu.itis.paramonov.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.paramonov.config.JwtPropertiesConfig;
import ru.kpfu.itis.paramonov.entity.User;

import javax.crypto.SecretKey;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private static final int JWT_TOKEN_EXPIRATION_TIME_MINUTES = 10;

    private static final int REMEMBER_JWT_TOKEN_EXPIRATION_TIME_DAYS = 30;

    private final JwtPropertiesConfig jwtPropertiesConfig;

    public String generateToken(User user, boolean remember) {
        LocalDateTime now = LocalDateTime.now();
        Instant accessExpirationTime;
        if (remember) {
            accessExpirationTime = now.plusDays(REMEMBER_JWT_TOKEN_EXPIRATION_TIME_DAYS)
                    .atZone(ZoneId.systemDefault()).toInstant();
        } else {
            accessExpirationTime = now.plusMinutes(JWT_TOKEN_EXPIRATION_TIME_MINUTES)
                    .atZone(ZoneId.systemDefault()).toInstant();
        }
        Date accessExpiration = Date.from(accessExpirationTime);
        return Jwts.builder()
                .id(user.getId().toString())
                .subject(user.getUsername())
                .expiration(accessExpiration)
                .signWith(getKey())
                .claim("roles", user.getRoles())
                .claim("id", user.getId())
                .compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtPropertiesConfig.getSecret()));
    }
}