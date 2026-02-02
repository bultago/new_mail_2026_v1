package com.nugenmail.domain.mail.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Mail Folder Model.
 * Represents a mail folder with message counts.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MailFolder {
    private String fullName;
    private String name;
    private int messageCount;
    private int unreadMessageCount;
}
