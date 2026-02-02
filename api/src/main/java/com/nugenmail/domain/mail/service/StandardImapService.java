package com.nugenmail.domain.mail.service;

import com.nugenmail.domain.mail.model.AttachmentItem;
import com.nugenmail.domain.mail.model.MailDetail;
import com.nugenmail.domain.mail.model.MailFolder;
import com.nugenmail.domain.mail.model.MailMessage;
import jakarta.mail.Address;
import jakarta.mail.BodyPart;
import jakarta.mail.FetchProfile;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Part;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.UIDFolder;
import jakarta.mail.internet.MimeUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.util.stream.Collectors;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;
import com.nugenmail.domain.mail.mapper.FolderPolicyMapper;
import com.nugenmail.domain.mail.mapper.SharedFolderMapper;
import com.nugenmail.domain.mail.model.FolderPolicy;
import com.nugenmail.domain.mail.model.SharedFolder;

/**
 * Standard IMAP Service implementation.
 * Handles folder management, message retrieval, and attachments via JavaMail
 * IMAP.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class StandardImapService implements MailServiceProvider {

    private final FolderPolicyMapper folderPolicyMapper;
    private final SharedFolderMapper sharedFolderMapper;

    @Value("${spring.mail.host:localhost}")
    private String mailHost;

    private Session createSession() {
        Properties props = new Properties();
        props.put("mail.store.protocol", "imap");
        props.put("mail.imap.host", mailHost);
        return Session.getInstance(props);
    }

    private Store connect(String username, String password) throws MessagingException {
        Session session = createSession();
        Store store = session.getStore("imap");
        store.connect(username, password);
        return store;
    }

    // --- Folder Operations ---

    /**
     * Retrieves the list of folders from the IMAP server.
     * Ensures default folders (Sent, Drafts, Trash) exist.
     */
    @Override
    public List<MailFolder> getFolderList(String username, String password) throws MessagingException {
        Store store = connect(username, password);
        try {
            ensureDefaultFolders(store);
            List<MailFolder> mailFolders = new ArrayList<>();
            Folder defaultFolder = store.getDefaultFolder();

            for (Folder folder : defaultFolder.list("*")) {
                if ((folder.getType() & Folder.HOLDS_MESSAGES) != 0) {
                    mailFolders.add(MailFolder.builder()
                            .fullName(folder.getFullName())
                            .name(folder.getName())
                            .build());
                }
            }
            return mailFolders;
        } finally {
            store.close();
        }
    }

    private void ensureDefaultFolders(Store store) throws MessagingException {
        String[] defaults = { "Sent", "Drafts", "Trash" };
        for (String name : defaults) {
            Folder f = store.getFolder(name);
            if (!f.exists()) {
                try {
                    f.create(Folder.HOLDS_MESSAGES);
                } catch (Exception e) {
                    // Ignore creation failure for now
                }
            }
        }
    }

    /**
     * Retrieves a specific folder's details.
     */
    @Override
    public MailFolder getFolder(String username, String password, String folderName) throws MessagingException {
        Store store = connect(username, password);
        try {
            Folder folder = store.getFolder(folderName);
            if (!folder.exists()) {
                throw new MessagingException("Folder not found: " + folderName);
            }
            return MailFolder.builder()
                    .name(folder.getName())
                    .fullName(folder.getFullName())
                    .build();
        } finally {
            store.close();
        }
    }

    /**
     * Creates a new folder.
     */
    @Override
    public void createFolder(String username, String password, String folderName) throws MessagingException {
        Store store = connect(username, password);
        try {
            Folder folder = store.getFolder(folderName);
            if (!folder.exists()) {
                if (!folder.create(Folder.HOLDS_MESSAGES)) {
                    throw new MessagingException("Failed to create folder: " + folderName);
                }
            }
        } finally {
            store.close();
        }
    }

    /**
     * Deletes a folder.
     */
    @Override
    public void deleteFolder(String username, String password, String folderName) throws MessagingException {
        Store store = connect(username, password);
        try {
            Folder folder = store.getFolder(folderName);
            if (folder.exists()) {
                if (!folder.delete(true)) {
                    throw new MessagingException("Failed to delete folder: " + folderName);
                }
            }
        } finally {
            store.close();
        }
    }

    /**
     * Renames a folder.
     */
    @Override
    public void renameFolder(String username, String password, String oldName, String newName)
            throws MessagingException {
        Store store = connect(username, password);
        try {
            Folder folder = store.getFolder(oldName);
            Folder newFolder = store.getFolder(newName);
            if (folder.exists()) {
                if (!folder.renameTo(newFolder)) {
                    throw new MessagingException("Failed to rename folder");
                }
            }
        } finally {
            store.close();
        }
    }

    /**
     * Moves a folder to a new parent.
     */
    @Override
    public void moveFolder(String username, String password, String folderName, String newParentName)
            throws MessagingException {
        Store store = connect(username, password);
        try {
            Folder folder = store.getFolder(folderName);
            if (!folder.exists()) {
                throw new MessagingException("Source folder not found: " + folderName);
            }

            char separator = store.getDefaultFolder().getSeparator();
            String simpleName = folder.getName();
            String newFullName;

            if (newParentName == null || newParentName.isEmpty() || "INBOX".equalsIgnoreCase(newParentName)) {
                newFullName = simpleName;
            } else {
                newFullName = newParentName + separator + simpleName;
            }

            if (newFullName.equals(folder.getFullName()) || newFullName.startsWith(folder.getFullName() + separator)) {
                throw new MessagingException("Cannot move folder into itself");
            }

            Folder newFolder = store.getFolder(newFullName);
            if (newFolder.exists()) {
                throw new MessagingException("Destination folder already exists: " + newFullName);
            }

            if (!folder.renameTo(newFolder)) {
                throw new MessagingException("Failed to move folder");
            }
        } finally {
            store.close();
        }
    }

    // --- Legacy Features (Backup, Aging, Shared) ---

    private final ConcurrentHashMap<String, String> backupStatusMap = new ConcurrentHashMap<>();

    /**
     * Starts an asynchronous folder backup job.
     * Logic: Creates a ZIP file of all messages in the folder.
     */
    @Override
    public void startFolderBackup(String username, String password, String folderName) throws MessagingException {
        // Generate a unique job ID or use "username_folderName" key
        String jobKey = username + "_" + folderName;

        if ("RUNNING".equals(backupStatusMap.get(jobKey))) {
            throw new MessagingException("Backup already in progress for this folder");
        }

        backupStatusMap.put(jobKey, "RUNNING");

        // Async Execution
        CompletableFuture.runAsync(() -> {
            Store store = null;
            Folder folder = null;
            try {
                store = connect(username, password);
                folder = store.getFolder(folderName);
                if (!folder.exists()) {
                    backupStatusMap.put(jobKey, "ERROR: Folder not found");
                    return;
                }
                folder.open(Folder.READ_ONLY);

                String tempDir = System.getProperty("java.io.tmpdir");
                String fileName = "backup_" + username + "_" + folderName + "_" + System.currentTimeMillis() + ".zip";
                java.nio.file.Path zipPath = java.nio.file.Paths.get(tempDir, fileName);

                try (java.io.FileOutputStream fos = new java.io.FileOutputStream(zipPath.toFile());
                        ZipOutputStream zos = new ZipOutputStream(fos)) {

                    Message[] messages = folder.getMessages();
                    FetchProfile fp = new FetchProfile();
                    fp.add(UIDFolder.FetchProfileItem.UID);
                    fp.add(FetchProfile.Item.CONTENT_INFO);
                    folder.fetch(messages, fp);

                    for (Message msg : messages) {
                        try {
                            // Create entry for each email, e.g., "uid_subject.eml"
                            String safeSubject = "no_subject";
                            if (msg.getSubject() != null) {
                                safeSubject = msg.getSubject().replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
                                if (safeSubject.length() > 50)
                                    safeSubject = safeSubject.substring(0, 50);
                            }

                            // Get UID if available
                            String uidStr = String.valueOf(msg.getMessageNumber());
                            if (folder instanceof UIDFolder) {
                                uidStr = String.valueOf(((UIDFolder) folder).getUID(msg));
                            }

                            String entryName = uidStr + "_" + safeSubject + ".eml";
                            zos.putNextEntry(new ZipEntry(entryName));
                            msg.writeTo(zos);
                            zos.closeEntry();
                        } catch (Exception e) {
                            log.warn("Failed to backup message", e);
                            // Continue with next message
                        }
                    }
                }

                // Store success status with file path
                backupStatusMap.put(jobKey, "SUCCESS:" + zipPath.toAbsolutePath().toString());

            } catch (Exception e) {
                log.error("Backup failed", e);
                backupStatusMap.put(jobKey, "ERROR:" + e.getMessage());
            } finally {
                if (folder != null) {
                    try {
                        folder.close(false);
                    } catch (Exception e) {
                    }
                }
                if (store != null) {
                    try {
                        store.close();
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    /**
     * Checks the status of a backup job.
     */
    @Override
    public String getFolderBackupStatus(String username, String password, String folderName) throws MessagingException {
        String jobKey = username + "_" + folderName;
        String status = backupStatusMap.getOrDefault(jobKey, "NONE");

        if (status.startsWith("SUCCESS:")) {
            return "{\"status\": \"COMPLETED\", \"message\": \"Backup ready\"}";
        } else if (status.startsWith("ERROR:")) {
            return "{\"status\": \"ERROR\", \"message\": \"" + status.substring(6) + "\"}";
        } else if ("RUNNING".equals(status)) {
            return "{\"status\": \"RUNNING\", \"message\": \"Processing...\"}";
        }

        return "{\"status\": \"NONE\", \"message\": \"No backup found\"}";
    }

    /**
     * Updates folder expiration (aging) policy.
     * Saves to database via FolderPolicyMapper.
     */
    @Override
    public void updateFolderAging(String username, String password, String folderName, int days)
            throws MessagingException {
        // Standard Mode: Save policy to DB
        // Assuming username is "user@domain" or just "user"
        String[] parts = username.split("@");
        String userId = parts[0];
        String domain = parts.length > 1 ? parts[1] : "default.com";

        // Validate folder exists first
        Store store = connect(username, password);
        try {
            if (!store.getFolder(folderName).exists())
                throw new MessagingException("Folder not found");
        } finally {
            store.close();
        }

        // Check if plain system folder
        String sysColumn = null;
        if ("INBOX".equalsIgnoreCase(folderName))
            sysColumn = "inbox_expire_days";
        else if ("Sent".equalsIgnoreCase(folderName))
            sysColumn = "sent_expire_days";
        else if ("Trash".equalsIgnoreCase(folderName))
            sysColumn = "trash_expire_days";
        else if ("Spam".equalsIgnoreCase(folderName))
            sysColumn = "spam_expire_days";
        else if ("User".equalsIgnoreCase(folderName))
            sysColumn = "user_expire_days";

        if (sysColumn != null) {
            folderPolicyMapper.updateSystemPolicy(userId, domain, sysColumn, days);
        } else {
            FolderPolicy existing = folderPolicyMapper.findPolicy(userId, domain, folderName);
            if (existing != null) {
                existing.setKeepDays(days);
                folderPolicyMapper.updatePolicy(existing);
            } else {
                FolderPolicy newPolicy = FolderPolicy.builder()
                        .userId(userId)
                        .domain(domain)
                        .folderName(folderName)
                        .keepDays(days)
                        .build();
                folderPolicyMapper.insertPolicy(newPolicy);
            }
        }
    }

    /**
     * Retrieves shared folders visible to the user.
     */
    @Override
    public List<String> getSharedFolders(String username, String password) throws MessagingException {
        String[] parts = username.split("@");
        String userId = parts[0];
        String domain = parts.length > 1 ? parts[1] : "default.com";

        List<SharedFolder> shared = sharedFolderMapper.findSharedFolders(userId, domain);
        return shared.stream()
                .map(sf -> sf.getOwnerName() + " (" + sf.getOwnerId() + "): " + sf.getFolderName())
                .collect(Collectors.toList());
    }

    // --- Message Operations ---

    /**
     * Retrieves top 20 messages from a folder.
     * Fetches ENVELOPE and UID.
     */
    @Override
    public List<MailMessage> getMessages(String username, String password, String folderName)
            throws MessagingException {
        Store store = connect(username, password);
        try {
            Folder folder = store.getFolder(folderName);
            if (!folder.exists())
                throw new MessagingException("Folder not found: " + folderName);

            folder.open(Folder.READ_ONLY);

            int messageCount = folder.getMessageCount();
            int start = Math.max(1, messageCount - 19);
            int end = messageCount;

            List<MailMessage> mailMessages = new ArrayList<>();
            if (messageCount > 0) {
                Message[] messages = folder.getMessages(start, end);
                FetchProfile fp = new FetchProfile();
                fp.add(FetchProfile.Item.ENVELOPE);
                fp.add(UIDFolder.FetchProfileItem.UID);
                folder.fetch(messages, fp);

                UIDFolder uidFolder = (folder instanceof UIDFolder) ? (UIDFolder) folder : null;

                for (int i = messages.length - 1; i >= 0; i--) {
                    Message msg = messages[i];
                    long uid = (uidFolder != null) ? uidFolder.getUID(msg) : i;

                    boolean hasAttachment = false;
                    try {
                        if (msg.isMimeType("multipart/*")) {
                            Multipart mp = (Multipart) msg.getContent();
                            for (int k = 0; k < mp.getCount(); k++) {
                                BodyPart bp = mp.getBodyPart(k);
                                String disp = bp.getDisposition();
                                if (Part.ATTACHMENT.equalsIgnoreCase(disp) || Part.INLINE.equalsIgnoreCase(disp)) {
                                    hasAttachment = true;
                                    break;
                                }
                            }
                        }
                    } catch (Exception e) {
                        // ignore
                    }

                    mailMessages.add(MailMessage.builder()
                            .uid(uid)
                            .subject(msg.getSubject())
                            .from(msg.getFrom() != null && msg.getFrom().length > 0 ? msg.getFrom()[0].toString()
                                    : "Unknown")
                            .sentDate(msg.getSentDate())
                            .seen(msg.getFlags().contains(jakarta.mail.Flags.Flag.SEEN))
                            .hasAttachment(hasAttachment)
                            .build());
                }
            }
            folder.close(false);
            return mailMessages;
        } finally {
            store.close();
        }
    }

    /**
     * Retrieves full details of a specific message by UID.
     * Parses structure to find attachments and body content.
     */
    @Override
    public MailDetail getMessageDetail(String username, String password, String folderName, long uid)
            throws MessagingException, IOException {
        Store store = connect(username, password);
        try {
            Folder folder = store.getFolder(folderName);
            if (!folder.exists())
                throw new MessagingException("Folder not found");

            folder.open(Folder.READ_ONLY);

            UIDFolder uidFolder = (folder instanceof UIDFolder) ? (UIDFolder) folder : null;
            if (uidFolder == null)
                throw new MessagingException("UIDs not supported");

            Message msg = uidFolder.getMessageByUID(uid);
            if (msg == null)
                throw new MessagingException("Message not found");

            List<String> toList = new ArrayList<>();
            if (msg.getRecipients(Message.RecipientType.TO) != null) {
                toList = Arrays.stream(msg.getRecipients(Message.RecipientType.TO))
                        .map(Address::toString)
                        .collect(Collectors.toList());
            }

            List<String> ccList = new ArrayList<>();
            if (msg.getRecipients(Message.RecipientType.CC) != null) {
                ccList = Arrays.stream(msg.getRecipients(Message.RecipientType.CC))
                        .map(Address::toString)
                        .collect(Collectors.toList());
            }

            List<AttachmentItem> attachments = new ArrayList<>();
            StringBuilder contentBuilder = new StringBuilder();

            extractContent(msg, contentBuilder, attachments, "");

            folder.close(false);

            return MailDetail.builder()
                    .uid(uid)
                    .subject(msg.getSubject())
                    .from(msg.getFrom() != null && msg.getFrom().length > 0 ? msg.getFrom()[0].toString() : "Unknown")
                    .to(toList)
                    .cc(ccList)
                    .sentDate(msg.getSentDate())
                    .content(contentBuilder.toString())
                    .attachments(attachments)
                    .build();

        } finally {
            store.close();
        }
    }

    private void extractContent(Part part, StringBuilder content, List<AttachmentItem> attachments, String partId)
            throws MessagingException, IOException {
        if (part.isMimeType("text/plain")) {
            if (content.length() == 0) {
                content.append(part.getContent().toString());
            }
        } else if (part.isMimeType("text/html")) {
            content.setLength(0);
            content.append(part.getContent().toString());
        } else if (part.isMimeType("multipart/*")) {
            Multipart multi = (Multipart) part.getContent();
            for (int i = 0; i < multi.getCount(); i++) {
                String nextId = partId.isEmpty() ? String.valueOf(i + 1) : partId + "." + (i + 1);
                extractContent(multi.getBodyPart(i), content, attachments, nextId);
            }
        } else if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())
                || Part.INLINE.equalsIgnoreCase(part.getDisposition())) {
            if (part.getFileName() != null) {
                attachments.add(AttachmentItem.builder()
                        .name(MimeUtility.decodeText(part.getFileName()))
                        .size(part.getSize())
                        .mimeType(part.getContentType())
                        .partId(partId)
                        .build());
            }
        }
    }

    // --- Attachment Operations ---

    /**
     * Downloads a single attachment.
     */
    @Override
    public void downloadAttachment(String username, String password, String folderName, long uid, String partId,
            OutputStream output) throws MessagingException, IOException {
        Store store = connect(username, password);
        try {
            Folder folder = store.getFolder(folderName);
            if (!folder.exists())
                throw new MessagingException("Folder not found");
            folder.open(Folder.READ_ONLY);

            UIDFolder uidFolder = (folder instanceof UIDFolder) ? (UIDFolder) folder : null;
            if (uidFolder == null)
                throw new MessagingException("UIDs not supported");

            Message msg = uidFolder.getMessageByUID(uid);
            if (msg == null)
                throw new MessagingException("Message not found");

            // Traverse to the part
            Part part = msg;
            if (partId != null && !partId.isEmpty()) {
                String[] indexes = partId.split("\\.");
                for (String idxStr : indexes) {
                    int idx = Integer.parseInt(idxStr) - 1; // 1-based to 0-based
                    if (part.isMimeType("multipart/*")) {
                        Multipart multi = (Multipart) part.getContent();
                        part = multi.getBodyPart(idx);
                    } else {
                        throw new MessagingException(
                                "Expected multipart for ID " + partId + " but found " + part.getContentType());
                    }
                }
            }

            try (InputStream is = part.getInputStream()) {
                FileCopyUtils.copy(is, output);
            }
            folder.close(false);
        } finally {
            store.close();
        }
    }

    /**
     * Downloads all attachments as a ZIP file.
     */
    @Override
    public void downloadAllAttachments(String username, String password, String folderName, long uid,
            ZipOutputStream zos) throws MessagingException, IOException {
        Store store = connect(username, password);
        try {
            Folder folder = store.getFolder(folderName);
            if (!folder.exists())
                throw new MessagingException("Folder not found");

            folder.open(Folder.READ_ONLY);

            UIDFolder uidFolder = (folder instanceof UIDFolder) ? (UIDFolder) folder : null;
            if (uidFolder == null)
                throw new MessagingException("UIDs not supported");

            Message msg = uidFolder.getMessageByUID(uid);
            if (msg == null)
                throw new MessagingException("Message not found");

            Object content = msg.getContent();
            traverseAndZip(content, zos);
            zos.finish();

            folder.close(false);

        } finally {
            store.close();
        }
    }

    private void traverseAndZip(Object content, ZipOutputStream zos) throws MessagingException, IOException {
        if (content instanceof Multipart) {
            Multipart multi = (Multipart) content;
            for (int i = 0; i < multi.getCount(); i++) {
                BodyPart part = multi.getBodyPart(i);

                if (part.isMimeType("message/rfc822")) {
                    String fileName = part.getFileName();
                    if (fileName == null)
                        fileName = "nested_message_" + i + ".eml";
                    else
                        fileName = MimeUtility.decodeText(fileName);

                    ZipEntry entry = new ZipEntry(fileName);
                    zos.putNextEntry(entry);
                    try (InputStream is = part.getInputStream()) {
                        FileCopyUtils.copy(is, zos);
                    }
                    zos.closeEntry();

                } else if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())
                        || Part.INLINE.equalsIgnoreCase(part.getDisposition())) {
                    String fileName = part.getFileName();
                    if (fileName != null) {
                        fileName = MimeUtility.decodeText(fileName);
                        ZipEntry entry = new ZipEntry(fileName);
                        zos.putNextEntry(entry);
                        try (InputStream is = part.getInputStream()) {
                            FileCopyUtils.copy(is, zos);
                        }
                        zos.closeEntry();
                    }
                } else if (part.isMimeType("multipart/*")) {
                    traverseAndZip(part.getContent(), zos);
                }
            }
        }
    }
}
