package com.huce.project.config;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.huce.project.service.Impl.AdminDetailsImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class jwtConfigs {
    @Value("${pdt.app.jwtSecret}")
    private String JWT_SECRET;

    @Value("${pdt.app.jwtExpirationMs}")
    private int JWT_EXPIRATION_MS;

    public String generateJwtToken(Authentication authentication) {
        AdminDetailsImpl userPrincipal = (AdminDetailsImpl) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + JWT_EXPIRATION_MS))
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            System.out.println(ex);
        } catch (MalformedJwtException ex) {
            System.out.println(ex);
        } catch (ExpiredJwtException ex) {
            System.out.println("Validated exception: ");
            System.out.println(ex);
        } catch (UnsupportedJwtException ex) {
            System.out.println(ex);
        } catch (IllegalArgumentException ex) {
            System.out.println(ex);
        }

        return false;
    }
}
