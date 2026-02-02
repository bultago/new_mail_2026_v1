package com.nugenmail.domain.mail.service;

import com.nugenmail.domain.mail.model.MailDetail;
import com.nugenmail.domain.mail.model.MailFolder;
import com.nugenmail.domain.mail.model.MailMessage;
import jakarta.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipOutputStream;

/**
 * Mail Service Provider Interface.
 * Defines standard operations for Mail Services (IMAP, etc).
 */
public interface MailServiceProvider {

        // --- Basic IMAP Operations ---

        /**
         * Get folder list.
         */
        List<MailFolder> getFolderList(String username, String password) throws MessagingException;

        /**
         * Get specific folder details.
         */
        MailFolder getFolder(String username, String password, String folderName) throws MessagingException;

        /**
         * Get messages from folder.
         */
        List<MailMessage> getMessages(String username, String password, String folderName) throws MessagingException;

        /**
         * Get message details.
         */
        MailDetail getMessageDetail(String username, String password, String folder, long uid)
                        throws MessagingException, IOException;

        /**
         * Download attachment.
         */
        void downloadAttachment(String username, String password, String folderName, long uid, String partId,
                        java.io.OutputStream outputStream) throws MessagingException, IOException;

        void downloadAllAttachments(String username, String password, String folderName, long uid, ZipOutputStream zos)
                        throws MessagingException, IOException;

        // --- Folder Operations ---
        void createFolder(String username, String password, String folderName) throws MessagingException;

        void deleteFolder(String username, String password, String folderName) throws MessagingException;

        void renameFolder(String username, String password, String oldName, String newName) throws MessagingException;

        void moveFolder(String username, String password, String folderName, String newParentName)
                        throws MessagingException;

        // --- Legacy / Advanced Features ---
        void startFolderBackup(String username, String password, String folderName) throws MessagingException;

        String getFolderBackupStatus(String username, String password, String folderName) throws MessagingException;

        void updateFolderAging(String username, String password, String folderName, int days) throws MessagingException;

        List<String> getSharedFolders(String username, String password) throws MessagingException;
}
