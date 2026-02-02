package com.nugenmail.domain.mail.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

/**
 * Mail Message Header Model.
 * Represents a summary of an email message.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MailMessage {
    private long uid;
    private String subject;
    private String from;
    private String to;
    private Date sentDate;
    private boolean seen;
    private boolean hasAttachment;
    private String snippet;
}
