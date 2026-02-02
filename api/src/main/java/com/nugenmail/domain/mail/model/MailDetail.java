package com.nugenmail.domain.mail.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.List;

/**
 * Mail Detail Model.
 * Contains full message content including body and attachments.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MailDetail {
    private long uid;
    private String subject;
    private String from;
    private List<String> to;
    private List<String> cc;
    private Date sentDate;
    private String content; // HTML or Text
    private List<AttachmentItem> attachments;
}
