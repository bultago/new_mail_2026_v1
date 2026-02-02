package com.nugenmail.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.nugenmail.domain.auth.service.AuthService;
import org.springframework.context.annotation.Lazy;

/**
 * JWT Token Provider
 * Handles creation and validation of JSON Web Tokens.
 */
@Component
public class JwtTokenProvider {

    private final AuthService authService;

    public JwtTokenProvider(@Lazy AuthService authService) {
        this.authService = authService;
    }

    @Value("${nugenmail.jwt.secret:defaultSecretKeyMuchLongerToSatisfyHS256BitRequirement}")
    private String secretKey;

    @Value("${nugenmail.jwt.expiration:3600000}") // 1 hour
    private long validityInMilliseconds;

    private Key key;

    @PostConstruct
    protected void init() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    /**
     * Creates a new JWT token.
     * 
     * @param userId User ID
     * @param domain Domain
     * @param jti    JWT ID (Session ID)
     * @return Generated JWT Token string
     */
    public String createToken(String userId, String domain, String jti) {
        Claims claims = Jwts.claims().setSubject(userId);
        claims.put("domain", domain);
        claims.setId(jti); // Standard "jti" claim

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Retrieves authentication information from a token.
     * 
     * @param token JWT Token
     * @return Authentication object
     */
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        String userId = claims.getSubject();

        // In a real scenario, we might reload user from DB, but for stateless JWT,
        // using claims is sufficient for most identity needs.
        User principal = new User(userId, "", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

        String domain = (String) claims.get("domain");
        String sessionId = (String) claims.get("sessionId"); // Assuming we put it in claims

        // Pass extra info if needed, or stick to simple Principal
        // We could create a custom UserDetails that holds domain/session
        return new UsernamePasswordAuthenticationToken(principal, token, principal.getAuthorities());
    }

    /**
     * Validates a token.
     * Checks signature and checks if session is active in DB.
     * 
     * @param token JWT Token
     * @return true if valid and active, false otherwise
     */
    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            String userId = claims.getSubject();
            String domain = (String) claims.get("domain");
            String jti = claims.getId();

            if (userId == null || domain == null || jti == null)
                return false;

            return authService.isTokenActive(userId, domain, jti);
        } catch (Exception e) {
            // Log exception in real implementation
            return false;
        }
    }

    /**
     * Parses and returns claims from a token.
     * 
     * @param token JWT Token
     * @return Claims object
     */
    public Claims getClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}
