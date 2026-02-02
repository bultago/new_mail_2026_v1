package com.nugenmail.domain.auth.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Authentication Mapper
 * MyBatis mapper for session and device management.
 */
@Mapper
public interface AuthMapper {

    /**
     * Inserts a new login session.
     * 
     * @param userId    User ID
     * @param domain    Domain
     * @param jti       JWT ID
     * @param clientIp  Client IP
     * @param userAgent User Agent
     */
    void insertSession(@Param("userId") String userId,
            @Param("domain") String domain,
            @Param("jti") String jti,
            @Param("clientIp") String clientIp,
            @Param("userAgent") String userAgent);

    /**
     * Counts active sessions with the given JTI.
     * 
     * @param jti JWT ID
     * @return count (0 or 1)
     */
    int countActiveJti(@Param("jti") String jti);

    /**
     * Deletes all sessions for a user (Single Session Enforcement).
     * 
     * @param userId User ID
     * @param domain Domain
     */
    void deleteAllSessions(@Param("userId") String userId, @Param("domain") String domain);

    /**
     * Deletes a specific session.
     * 
     * @param jti JWT ID
     */
    void deleteSessionByJti(@Param("jti") String jti);

    /**
     * Retrieves all active sessions for a user.
     * 
     * @param userId User ID
     * @param domain Domain
     * @return List of sessions
     */
    java.util.List<java.util.Map<String, Object>> getSessions(@Param("userId") String userId,
            @Param("domain") String domain);

    /**
     * Logs an action in the history table.
     * 
     * @param userId    User ID
     * @param domain    Domain
     * @param jti       JWT ID
     * @param clientIp  Client IP
     * @param userAgent User Agent
     * @param action    Action name (LOGIN, LOGOUT, etc.)
     */
    void insertHistory(@Param("userId") String userId,
            @Param("domain") String domain,
            @Param("jti") String jti,
            @Param("clientIp") String clientIp,
            @Param("userAgent") String userAgent,
            @Param("action") String action);

    /**
     * Checks if a device is registered and allowed.
     * 
     * @param userId    User ID
     * @param domain    Domain
     * @param userAgent User Agent
     * @return true if allowed, false otherwise
     */
    boolean isDeviceAllowed(@Param("userId") String userId,
            @Param("domain") String domain,
            @Param("userAgent") String userAgent);

    /**
     * Registers a new device.
     * 
     * @param userId     User ID
     * @param domain     Domain
     * @param deviceName Friendly Device Name
     * @param userAgent  User Agent
     */
    void registerDevice(@Param("userId") String userId,
            @Param("domain") String domain,
            @Param("deviceName") String deviceName,
            @Param("userAgent") String userAgent);
}
