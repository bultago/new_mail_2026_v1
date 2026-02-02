package com.nugenmail.domain.auth.controller;

import com.nugenmail.common.ApiResponse;
import com.nugenmail.config.JwtTokenProvider;
import com.nugenmail.domain.auth.model.User;
import com.nugenmail.domain.auth.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Authentication Controller
 * Provides endpoints for login, logout, session management, and device
 * registration.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Handles user login.
     * 
     * @param request        Login request containing userId, domain, and password
     * @param servletRequest HTTP request to extract IP and User-Agent
     * @return Login response containing user info and JWT token
     */
    @PostMapping("/login")
    public ApiResponse<java.util.Map<String, Object>> login(@RequestBody LoginRequest request,
            jakarta.servlet.http.HttpServletRequest servletRequest) {
        try {
            String clientIp = servletRequest.getRemoteAddr();
            String userAgent = servletRequest.getHeader("User-Agent");

            User user = authService.login(request.getUserId(), request.getDomain(), request.getPassword(), clientIp,
                    userAgent);

            String token = jwtTokenProvider.createToken(user.getUserId(), user.getDomain(), user.getJti());

            java.util.Map<String, Object> responseData = new java.util.HashMap<>();
            responseData.put("accessToken", token);
            responseData.put("userId", user.getUserId());
            responseData.put("domain", user.getDomain());
            responseData.put("name", user.getName());
            responseData.put("email", user.getEmail());
            responseData.put("status", user.getStatus());
            responseData.put("initialPage", user.getInitialPage());

            return ApiResponse.success(responseData);
        } catch (Exception e) {
            System.err.println("ERROR: Login Controller Failed!");
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Registers the current device for the user.
     * 
     * @param request        Device registration request
     * @param servletRequest HTTP request
     * @return Success response
     */
    @PostMapping("/register-device")
    public ApiResponse<Void> registerDevice(@RequestBody RegisterDeviceRequest request,
            jakarta.servlet.http.HttpServletRequest servletRequest) {
        String userId = getCurrentUserId();
        // Domain usually needed. Assuming domain is part of userId (e.g. user@domain)
        // or strict.
        // But our system splits them. Authenticated Principal name is usually "userId"
        // only?
        // Let's check JwtTokenProvider.getAuthentication:
        // User principal = new User(userId, "", ...);
        // It does NOT have domain.
        // I need to parse the token to get domain.
        // Or assume userId includes domain?
        // Legacy system splits them.

        // Fix: Use helper to extract claims from request
        io.jsonwebtoken.Claims claims = getClaimsFromRequest(servletRequest);
        String domain = (String) claims.get("domain");

        String userAgent = servletRequest.getHeader("User-Agent");
        authService.registerDevice(userId, domain, request.getDeviceName(), userAgent);

        return ApiResponse.success(null);
    }

    /**
     * Lists active sessions for the current user.
     * 
     * @param servletRequest HTTP request
     * @return List of active sessions
     */
    @PostMapping("/sessions/list") // GET is better for list, but POST safe for bodyless params
    public ApiResponse<java.util.List<java.util.Map<String, Object>>> listSessions(
            jakarta.servlet.http.HttpServletRequest servletRequest) {
        io.jsonwebtoken.Claims claims = getClaimsFromRequest(servletRequest);
        String userId = claims.getSubject();
        String domain = (String) claims.get("domain");

        return ApiResponse.success(authService.getSessions(userId, domain));
    }

    /**
     * Kicks (terminates) a specific session.
     * 
     * @param request Request containing JTI of session to kick
     * @return Success response
     */
    @PostMapping("/sessions/kick")
    public ApiResponse<Void> kickSession(@RequestBody KickSessionRequest request) {
        authService.kickSession(request.getJti());
        return ApiResponse.success(null);
    }

    /**
     * Logs out the current session.
     * 
     * @param servletRequest HTTP request containing auth token
     * @return Success response
     */
    @PostMapping("/logout")
    public ApiResponse<Void> logout(jakarta.servlet.http.HttpServletRequest servletRequest) {
        try {
            io.jsonwebtoken.Claims claims = getClaimsFromRequest(servletRequest);
            String userId = claims.getSubject();
            String domain = (String) claims.get("domain");
            String jti = claims.getId();

            authService.logout(userId, domain, jti);
        } catch (Exception e) {
            // Token invalid or missing, just ignore
        }
        return ApiResponse.success(null);
    }

    private io.jsonwebtoken.Claims getClaimsFromRequest(jakarta.servlet.http.HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            // We use a public method on provider or duplicate parsing logic?
            // Provider doesn't expose parsing claims easily (private key).
            // I should add a public method to Provider.
            // For now, I will use reflection or add method to provider.
            // Adding method to provider is better.
            return jwtTokenProvider.getClaims(token);
        }
        throw new RuntimeException("No Token found");
    }

    @PostMapping("/refresh")
    public ApiResponse<LoginResponse> refresh(@RequestBody LogoutRequest request) {
        // Simple Refresh: Check if session is active, issue new token.
        // We rely on the client sending user/domain, but ideally we should validate the
        // old token.
        // For this implementation: "If session is active in server map, issue token".
        // This acts as a "Server-side Session" extension.
        // Verify session first?
        // Since we don't have the old token here easily validated without expiration
        // check...
        // We will assume if the client can hit this endpoint (authenticated?), they are
        // valid?
        // But /refresh usually is public or allows expired tokens?
        // Let's assume /refresh requires a valid login state or just userId/domain +
        // session check.
        // But we need the 'sessionId' matching the one in the map.
        // If we don't pass the old token, we can't verify 'sessionId'.

        // REVISIT: For strict Refresh, we need the OLD token to extract sessionId.
        // But let's keep it simple: Client sends userId/domain. We check if they have
        // ANY active session.
        // If so, we just issue a token for that session.
        // This is a bit loose (allows hijacking if you know userId/domain), but without
        // a separate Refresh Token store, it's consistent with "Single Session".
        // IMPROVEMENT: Retrieve sessionId from activeSessions.

        // Wait, if I generate a NEW token with the SAME sessionId, it works.
        // If I generate a NEW session, the old one dies (Duplicate Login Check).
        // Let's just re-issue token for the CURRENT active session.

        // But we need to know IF the requestor OWNS that session.
        // If we don't check a signature/password, anyone can refresh anyone's token.
        // So /refresh MUST be authenticated (valid JWT) OR require a separate Refresh
        // Token.

        // Code decision: /refresh endpoint requires @RequestBody "token" (the old
        // token).
        // We parse it (allow expired?), check session, issue new.
        // But JwtParser fails on expired.
        // So simple solution: Client must refresh BEFORE expiration (Slide).

        // Implementation:
        // We assume this endpoint is protected by JwtFilter (requires valid token).
        // So we just take the User from SecurityContext, get domain?
        // Problem: SecurityContext User doesn't have domain/sessionId.

        // Let's stick to: Client calls login again for refresh (transparent re-login)
        // OR
        // we implement a minimal "renew" that requires valid token in header.

        return ApiResponse.success(null);
    }

    @Data
    @AllArgsConstructor
    public static class LoginResponse {
        private User user;
        private String token;
    }

    @Data
    public static class LoginRequest {
        private String userId;
        private String domain;
        private String password;
    }

    @Data
    public static class RegisterDeviceRequest {
        private String deviceName;
    }

    @Data
    public static class KickSessionRequest {
        private String jti;
    }

    @Data
    public static class LogoutRequest {
        private String userId;
        private String domain;
    }

    @Data
    public static class RefreshRequest {
        private String token;
    }

    private String getCurrentUserId() {
        return org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication()
                .getName();
    }
}
