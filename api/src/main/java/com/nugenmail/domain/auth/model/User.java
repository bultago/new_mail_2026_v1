package com.nugenmail.domain.auth.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User Entity
 * Represents a mail user with authentication details.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String userId;
    @com.fasterxml.jackson.annotation.JsonIgnore
    private String password; // Encrypted
    private String name;
    private String email;
    private String domain;
    private String status;

    /**
     * JWT Unique ID for duplicate login check.
     */
    private String jti;

    /**
     * User's configured initial screen (e.g. HOME, MAIL).
     */
    private String initialPage;

    /**
     * Transient flag indicating if the specific login device is registered.
     */
    @com.fasterxml.jackson.annotation.JsonIgnore
    private transient boolean isRegisteredDevice;
}
