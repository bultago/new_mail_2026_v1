package com.nugenmail.domain.auth.service;

import com.nugenmail.domain.auth.mapper.AuthMapper;
import com.nugenmail.domain.auth.mapper.UserMapper;
import com.nugenmail.domain.auth.model.User;
import com.nugenmail.util.PasswordUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Authentication Service
 * Handles user login/logout, session management, and device registration.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserMapper userMapper;
    private final AuthMapper authMapper;
    private final PasswordUtils passwordUtils;

    @Value("${nugenmail.security.device-restriction-enabled:false}")
    private boolean isDeviceRestrictionEnabled;

    // No more in-memory map

    /**
     * Authenticates a user and creates a new session.
     * 
     * @param userId    User ID
     * @param domain    Domain
     * @param password  Password
     * @param clientIp  Client IP Address
     * @param userAgent User Agent String
     * @return Authenticated User object with JTI
     * @throws IllegalArgumentException if authentication fails or device is
     *                                  restricted
     */
    @Transactional
    public User login(String userId, String domain, String password, String clientIp, String userAgent) {
        User user = userMapper.findByUserIdAndDomain(userId, domain);

        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        // 1. Legacy Password Verification
        if (!passwordUtils.checkPassword(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }

        // 2. Device Registration / Restriction Check
        boolean isDeviceAllowed = authMapper.isDeviceAllowed(userId, domain, userAgent);
        user.setRegisteredDevice(isDeviceAllowed);

        if (isDeviceRestrictionEnabled && !isDeviceAllowed) {
            throw new IllegalArgumentException("Access denied: Device not registered.");
        }

        // 3. Duplicate Login Limit Policy
        // If device is registered -> Allow Multi-session (Do nothing to existing
        // sessions)
        // If device is NOT registered -> Enforce Single-session (Delete all existing)
        if (!isDeviceAllowed) {
            authMapper.deleteAllSessions(userId, domain);
        }

        // Generate a new unique token ID (JTI)
        String newJti = java.util.UUID.randomUUID().toString();

        // Insert new session
        authMapper.insertSession(userId, domain, newJti, clientIp, userAgent);

        // Log History
        authMapper.insertHistory(userId, domain, newJti, clientIp, userAgent, "LOGIN");

        // Store JTI in user object to be used by Controller/JWT generator
        user.setJti(newJti);

        return user;
    }

    /**
     * Logs out a session.
     * 
     * @param userId User ID
     * @param domain Domain
     * @param jti    JWT ID to invalidate (optional, if null, no specific session
     *               removed here but history logged)
     */
    @Transactional
    public void logout(String userId, String domain, String jti) {
        // Logout specific session if JTI provided, or current context
        // But Logout usually means "Current".
        // If JTI is passed, remove that.
        if (jti != null) {
            authMapper.deleteSessionByJti(jti);
            authMapper.insertHistory(userId, domain, jti, null, null, "LOGOUT");
        }
    }

    /**
     * Forcefully terminates a session (Kick).
     * 
     * @param jti JWT ID of the session to kick
     */
    @Transactional
    public void kickSession(String jti) {
        // Admin/Self kick
        authMapper.deleteSessionByJti(jti);
    }

    /**
     * Registers a device for the user.
     * 
     * @param userId     User ID
     * @param domain     Domain
     * @param deviceName Friendly name for the device
     * @param userAgent  User Agent string to register
     */
    @Transactional
    public void registerDevice(String userId, String domain, String deviceName, String userAgent) {
        authMapper.registerDevice(userId, domain, deviceName, userAgent);
    }

    /**
     * Retrieves active sessions for a user.
     * 
     * @param userId User ID
     * @param domain Domain
     * @return List of session maps
     */
    @Transactional(readOnly = true)
    public java.util.List<java.util.Map<String, Object>> getSessions(String userId, String domain) {
        return authMapper.getSessions(userId, domain);
    }

    /**
     * Checks if a token is still active.
     * 
     * @param userId User ID
     * @param domain Domain
     * @param jti    JWT ID
     * @return true if active, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean isTokenActive(String userId, String domain, String jti) {
        // Optimizing: countActiveJti is faster than getActiveJti string
        return authMapper.countActiveJti(jti) > 0;
    }
}
