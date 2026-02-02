package com.nugenmail.domain.mail.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Shared Folder Model.
 * Represents a folder shared by another user.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SharedFolder {
    private String ownerId; // mail_uid of owner
    private String ownerName; // user_name of owner
    private String folderName; // shared folder name
}
