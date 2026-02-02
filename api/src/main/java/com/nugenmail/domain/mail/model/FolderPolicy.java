package com.nugenmail.domain.mail.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Folder Retention Policy.
 * Defines how long messages are kept in a specific folder.
 * Maps to legacy usage of userfolder_aging or mail_user_info columns.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FolderPolicy {
    // Legacy Table: mail_userfolder_aging
    // Columns: mail_user_seq, folder_name, aging_day

    // We don't store mail_user_seq in the model directly, but use userId/domain for
    // lookup
    private String userId; // mail_uid
    private String domain; // mail_domain_name
    private String folderName;
    private int keepDays; // aging_day
}
