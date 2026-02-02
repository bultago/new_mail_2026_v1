package com.nugenmail.domain.mail.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Attachment Metadata.
 * Represents a file attached to an email.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentItem {
    private String name;
    private long size;
    private String mimeType;
    private String partId;
}
