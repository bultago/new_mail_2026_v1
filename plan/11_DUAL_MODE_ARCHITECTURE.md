# Dual Mode Mail Architecture Strategy

## 1. Overview
As requested, the backend will support two distinct operational modes via an abstraction layer. This allows the system to operate on both standard 3rd party IMAP servers AND the specialized legacy Terrace Mail infrastructure.

## 2. Abstraction Layer
We will define a core interface `MailServiceProvider` that unifies all mail operations.

```java
public interface MailServiceProvider {
    // Basic
    List<MailFolder> getFolders(String user, String pwd);
    List<MailMessage> getMessages(String user, String pwd, String folder);
    
    // Legacy/Advanced Features
    void startFolderBackup(String user, String pwd, String folder); // Standard: Zip Stream, Legacy: Server Job
    void createBigAttachment(String user, String pwd, File file); // Standard: Reject/MIME, Legacy: Webfolder
    List<String> getSharedFolders(String user, String pwd); // Standard: ACL, Legacy: Ladmin
}
```

## 3. Implementation Strategies

### A. `StandardImapProvider` (The "RFC Compliant" Way)
- **Protocol**: Pure IMAP / SMTP.
- **Backup**: Client-side (Backend Proxy) ZIP streaming.
- **Aging**: Not supported or Client-side Scheduler + Search/Delete.
- **BigFile**: Standard MIME attachments (restricted by server size limit).
- **Target**: Gmail, Outlook, Dovecot, Postfix.

### B. `TerraceLegacyProvider` (The "Custom" Way)
- **Protocol**: IMAP + `Ladmin` (Proprietary) + Direct FS Access (optionally).
- **Backup**: Triggers server-side `FolderBackupJob` via `Ladmin` protocol.
- **Aging**: Updates `TB_USER_FOLDER_CONFIG` table directly.
- **BigFile**: Uploads to `/webfolder`, inserts `<a href...>` link.
- **Target**: Terrace Mail Servers (TMS).

## 4. Implementation Details for Unimplemented Features

### 4.1 Folder Backup
| Feature | Standard Mode Impl | Legacy Mode Impl |
| :--- | :--- | :--- |
| **Trigger** | `POST /backup` spawns `@Async` task. | `POST /backup` sends `Ladmin` CMD: `START_BACKUP`. |
| **Logic** | Iterates IMAP messages -> Writes to server-side `temp/util_zip` -> Updates status map. | Server engine handles logic. Backend polls status via `Ladmin`. |
| **Storage** | Temporary file on Backend Server (Ephemeral). | File generated on Mail Server (Persistent until download). |
| **Download** | Streaming from Backend Temp. | Proxy downloading from Mail Server path. |

### 4.2 Folder Aging (Retention)
| Feature | Standard Mode Impl | Legacy Mode Impl |
| :--- | :--- | :--- |
| **Config** | Save policy to `Local DB` (e.g. `FOLDER_RETENTION` table). | Save policy to Legacy DB (`TB_FOLDER_POLICY`). |
| **Enforcement**| Spring Scheduler (Daily).<br>1. Connect IMAP<br>2. `SEARCH BEFORE {Date}`<br>3. `STORE +Flags \Deleted`<br>4. `EXPUNGE` | (Option A) Rely on Legacy Engine's internal scheduler.<br>(Option B) Same as Standard Mode but targeting Legacy DB for config source. |

### 4.3 Shared Folders
| Feature | Standard Mode Impl | Legacy Mode Impl |
| :--- | :--- | :--- |
| **Discovery** | `NAMESPACE` command + `LIST` with permissions. | Query `TB_SHARED_MAILBOX` (Legacy DB) or `Ladmin` `GET_SHARED_LIST`. |
| **Access** | `SELECT "User/Sharer/Folder"` (Standard Namespace). | `LOGIN admin user` (Proxy Auth) or specific Legacy Folder syntax. |
| **Permissions**| `GETACL` / `SETACL` (RFC 4314). | Update `TB_ACL` table or `Ladmin` `SET_PERM`. |

### 4.4 Large Attachment (BigFile)
| Feature | Standard Mode Impl | Legacy Mode Impl |
| :--- | :--- | :--- |
| **Upload** | Rejects if > SMTP limit (e.g., 25MB). | Uploads to `/api/webfolder`. Stored in Shared Storage (`NAS`). |
| **Sending** | Sent as normal MIME attachment. | HTML Body Modification: Insert `<a href="download_link">Desc</a>`. |

## 5. Selection Mechanism
- **Global Config**: `application.properties` -> `mail.provider=standard` or `legacy`.
- **Hybrid**: Potential to switch based on User Domain or specific header if multiple backends exist.

## 6. Execution Steps
1.  **Approval**: Confirm this plan.
2.  **Refactor**: Split `ImapService` -> `MailServiceProvider` (Interface) + `StandardImapService` (Current Logic).
3.  **Implement**: Create `TerraceLegacyService` stub.
4.  **Fill Gaps**: Implement the logic described in Section 4 for each provider.

