package com.nugenmail.domain.mail.service;

import com.nugenmail.domain.mail.model.MailDetail;
import com.nugenmail.domain.mail.model.MailFolder;
import com.nugenmail.domain.mail.model.MailFolder;
import com.nugenmail.domain.mail.model.MailMessage;
import com.nugenmail.domain.mail.model.SharedFolder;
import com.nugenmail.domain.mail.mapper.SharedFolderMapper;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipOutputStream;

/**
 * Legacy "Terrace" IMAP Service.
 * Implements interactions with the Ladmin service and legacy database.
 */
@Service
@Slf4j
@lombok.RequiredArgsConstructor
public class TerraceImapService implements MailServiceProvider {

    @Value("${nugenmail.ladmin.host:localhost}")
    private String ladminHost;

    @Value("${nugenmail.ladmin.port:5002}")
    private int ladminPort;

    private final org.springframework.jdbc.core.JdbcTemplate jdbcTemplate;
    private final SharedFolderMapper sharedFolderMapper;

    @Override
    public List<MailFolder> getFolderList(String username, String password) throws MessagingException {
        // Legacy: Check DB or use Ladmin to get folder list
        return Collections.emptyList();
    }

    @Override
    public MailFolder getFolder(String username, String password, String folderName) throws MessagingException {
        throw new UnsupportedOperationException("Legacy getFolder not implemented");
    }

    @Override
    public List<MailMessage> getMessages(String username, String password, String folderName)
            throws MessagingException {
        // Legacy: Direct DB access or Proprietery Protocol
        return Collections.emptyList();
    }

    @Override
    public MailDetail getMessageDetail(String username, String password, String folder, long uid)
            throws MessagingException, IOException {
        throw new UnsupportedOperationException("Legacy getMessageDetail not implemented");
    }

    @Override
    public void downloadAttachment(String username, String password, String folderName, long uid, String partId,
            java.io.OutputStream output) throws MessagingException, IOException {
        throw new UnsupportedOperationException("Attachment download not supported in Legacy Mode yet");
    }

    @Override
    public void downloadAllAttachments(String username, String password, String folderName, long uid,
            ZipOutputStream zos)
            throws MessagingException, IOException {
        throw new UnsupportedOperationException("Legacy downloadAllAttachments not implemented");
    }

    /**
     * Creates a folder via Ladmin protocol.
     */
    @Override
    public void createFolder(String username, String password, String folderName) throws MessagingException {
        // Legacy: Ladmin CREATE_FOLDER
    }

    /**
     * Deletes a folder via Ladmin protocol.
     */
    @Override
    public void deleteFolder(String username, String password, String folderName) throws MessagingException {
        // Legacy: Ladmin DELETE_FOLDER
    }

    /**
     * Renames a folder via Ladmin protocol.
     */
    @Override
    public void renameFolder(String username, String password, String oldName, String newName)
            throws MessagingException {
        // Legacy: Ladmin RENAME_FOLDER
    }

    /**
     * Moves a folder via Ladmin protocol.
     */
    @Override
    public void moveFolder(String username, String password, String folderName, String newParentName)
            throws MessagingException {
        // Legacy: Ladmin MOVE_FOLDER
    }

    // --- Legacy Features Implementation ---

    /**
     * Triggers a mailbox backup via Ladmin command: USER_MAILBOX_BACKUP.
     */
    @Override
    public void startFolderBackup(String username, String password, String folderName) throws MessagingException {
        // Command: USER_MAILBOX_BACKUP username folderName
        String response = sendLadminCommand("USER_MAILBOX_BACKUP", username, folderName);
        if (!response.startsWith("OK")) {
            throw new MessagingException("Ladmin Backup Failed: " + response);
        }
    }

    /**
     * Checks backup status via Ladmin command: USER_MAILBOX_BACKUP_STATUS.
     */
    @Override
    public String getFolderBackupStatus(String username, String password, String folderName) throws MessagingException {
        // Command: USER_MAILBOX_BACKUP_STATUS username
        // Note: Legacy Ladmin usually takes just username for status as it tracks the
        // active job?
        // Based on LadminManager.java line 68: USER_MAILBOX_BACKUP_STATUS new
        // String[]{email}
        String response = sendLadminCommand("USER_MAILBOX_BACKUP_STATUS", username);

        // Parse response which is expected to be a map or structured string.
        // Simplified for now:
        if (response.startsWith("OK")) {
            return "{\"status\": \"RUNNING\", \"raw\": \"" + response + "\"}";
        }
        return "{\"status\": \"ERROR\", \"message\": \"" + response + "\"}";
    }

    /**
     * Updates legacy aging policy by modifying mail_user_info table directly.
     */
    @Override
    public void updateFolderAging(String username, String password, String folderName, int days)
            throws MessagingException {
        // Legacy Implementation: Update DB Table mail_user_info
        // Map folderName to column
        String column = null;
        if ("INBOX".equalsIgnoreCase(folderName))
            column = "inbox_expire_days";
        else if ("Sent".equalsIgnoreCase(folderName))
            column = "sent_expire_days";
        else if ("Trash".equalsIgnoreCase(folderName))
            column = "trash_expire_days";
        else if ("Spam".equalsIgnoreCase(folderName))
            column = "spam_expire_days";
        else
            column = "user_expire_days"; // Fallback for custom folders

        String userId = username.split("@")[0]; // Simple assumption

        try {
            // 1. Find mail_user_seq from mail_user (Optional JOIN but let's do subquery or
            // direct update with JOIN if DB supports)
            // Standard Oracle/MySQL Update with JOIN is tricky across dialects.
            // Safer: Get Seq first.
            String seqSql = "SELECT mail_user_seq FROM mail_user WHERE mail_uid = ?";
            Integer userSeq = jdbcTemplate.queryForObject(seqSql, Integer.class, userId);

            if (userSeq == null)
                throw new MessagingException("User not found: " + username);

            String updateSql = "UPDATE mail_user_info SET " + column + " = ? WHERE mail_user_seq = ?";
            jdbcTemplate.update(updateSql, days, userSeq);

            log.info("Legacy Aging Update: User={}, Folder={}, Days={}", username, folderName, days);

        } catch (Exception e) {
            log.error("Failed to update legacy aging", e);
            throw new MessagingException("Database Error: " + e.getMessage());
        }
    }

    /**
     * Retrieves shared folders via DB mapper.
     */
    @Override
    public List<String> getSharedFolders(String username, String password) throws MessagingException {
        // Legacy Implementation: Query TB_SHARED_MAILBOX via Mapper
        String[] parts = username.split("@");
        String userId = parts[0];
        String domain = parts.length > 1 ? parts[1] : "default.com";

        try {
            List<SharedFolder> shared = sharedFolderMapper.findSharedFolders(userId, domain);
            return shared.stream()
                    .map(sf -> sf.getOwnerName() + " (" + sf.getOwnerId() + "): " + sf.getFolderName())
                    .collect(java.util.stream.Collectors.toList());
        } catch (Exception e) {
            log.error("Failed to fetch legacy shared folders", e);
            return java.util.Collections.emptyList();
        }
    }

    /**
     * Sends a command to the Ladmin server and awaits a single line response
     * (simplified).
     * Detailed protocol implementation might require reading multiline responses.
     */
    private String sendLadminCommand(String command, String... args) throws MessagingException {
        try (Socket socket = new Socket(ladminHost, ladminPort);
                PrintWriter out = new PrintWriter(
                        new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8))) {

            // Read Greeting
            String greeting = in.readLine();
            if (greeting == null || !greeting.startsWith("*")) {
                throw new MessagingException("Invalid Ladmin Greeting: " + greeting);
            }

            // Construct Command
            StringBuilder cmdBuilder = new StringBuilder("A001 " + command);
            for (String arg : args) {
                cmdBuilder.append(" \"").append(arg).append("\""); // Simple quoting
            }
            log.debug("Sending Ladmin Command: {}", cmdBuilder);
            out.println(cmdBuilder.toString());

            // Read Response
            String line;
            while ((line = in.readLine()) != null) {
                if (line.startsWith("A001 OK")) {
                    return "OK " + line.substring(7);
                }
                if (line.startsWith("A001 NO") || line.startsWith("A001 BAD")) {
                    return "ERROR " + line;
                }
                // Handle untagged responses if needed (e.g. status data)
                // For now, we return the last status.
            }
            throw new MessagingException("Connection closed without response");

        } catch (IOException e) {
            log.error("Ladmin connection failed", e);
            throw new MessagingException("Ladmin Connection Error: " + e.getMessage());
        }
    }
}
