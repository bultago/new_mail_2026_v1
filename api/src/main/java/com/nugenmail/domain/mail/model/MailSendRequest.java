package com.nugenmail.domain.mail.model;

import lombok.Data;

/**
 * Mail Sending Request Body.
 * Encapsulates data required to send an email.
 */
@Data
public class MailSendRequest {
    private String to;
    private String subject;
    private String content;
    // Optional: for temporary MVP auth passing
    private String password;
}
