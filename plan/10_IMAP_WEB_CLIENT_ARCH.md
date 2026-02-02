# IMAP Web Client Architecture for TMS Mail (2026)

## 1. Vision & Strategy
Move beyond simple "Legacy Migration" to building a **Modern, Thick-Client Style Web Application** (like Outlook Web / Gmail) that supports both **Legacy Terrace Features** and **Standard IMAP Standards**.

### Core Principles
1.  **Dual Mode Support**:
    - **Terrace Mode**: Full support for proprietary extensions (Webfolder, BigFile, Ack).
    - **Standard Mode**: Compliant with RFC standards for 3rd party clients (Outlook, Thunderbird).
2.  **Client-Side Centric**:
    - The Web Client will maintain a **Local Sync Store** (IndexedDB) for performance.
    - Backward compatible with existing IMAP backend but accessed via **REST API Layer**.
3.  **Backend Modernization**:
    - The Backend acts as the "Sync Engine" and "Feature Provider" (e.g., BigFile Upload Handler).

---

## 2. Feature Implementation Strategy

### 2.1 Large Attachments (대용량 첨부)
Legacy Logic: Upload to Webfolder -> Generate `http://.../downloadBigAttach.do` link -> Insert into Body.

| Client Type | Strategy | Implementation Detail |
| :--- | :--- | :--- |
| **New Web Client** | **Emulate Legacy** | 1. Vue.js uploads file to `/api/v1/webfolder/upload`<br>2. Backend saves to shared storage<br>3. Vue.js inserts HTML link into Editor |
| **Standard Client** | **MIME Attachment** | 1. Outlook sends file as MIME part (via SMTP)<br>2. Server accepts (if within size limit) or rejects |

### 2.2 Read Receipt (수신확인)
Legacy Logic: `Disposition-Notification-To` header + MDN processing.

| Client Type | Strategy | Implementation Detail |
| :--- | :--- | :--- |
| **New Web Client** | **Standard MDN** | 1. Vue.js adds `Disposition-Notification-To` header<br>2. Backend tracks sent message ID<br>3. Receiver sends MDN -> Backend matches it |
| **Standard Client** | **Standard MDN** | 1. Outlook adds header<br>2. Backend processes MDN responses naturally |

### 2.3 Webfolder (자료실/웹폴더)
Legacy Logic: IMAP Folder access with specal encodings.

| Client Type | Strategy | Implementation Detail |
| :--- | :--- | :--- |
| **New Web Client** | **Dedicated API** | 1. Access via `/api/v1/webfolder/*`<br>2. Backend maps this to underlying IMAP or FS logic |
| **Standard Client** | **IMAP Folder** | 1. Server exposes Webfolder as "Shared Folders" namespace (if supported) |

---

## 3. Architecture Layers

### Frontend (Vue.js + Vite)
- **Sync Engine**: `tanstack-query` + `IndexedDB` (Dexie.js).
- **Mail Store**: Replicates IMAP folder structure locally.
- **Smart Editor**: Handles Large File drops -> converts to Links automatically.

### Backend (Spring Boot 6 / Legacy Core)
- **API Connectivity**:
    - `MailController`: `/api/v1/mail/...` (REST)
    - `WebBytesHandler`: Handles raw file streams.
- **Legacy Integration**:
    - Reuses `WebfolderManager` for file storage logic.
    - Reuses `MailServiceManager` for SMTP/IMAP protocol logic but wrapped in Services.

## 4. Workflows

### 4.1 Sending Mail (BigFile)
1.  **User**: Drags 1GB file into Composer.
2.  **Frontend**:
    - Detects file size > 10MB.
    - Calls `POST /api/v1/webfolder/upload/temp`.
    - Receives `fileId` and `downloadUrl`.
    - Inserts Template: `<a href="{downloadUrl}">Download 1GB File</a>`.
3.  **User**: Clicks Send.
4.  **Frontend**: POSTs JSON (Body + Recipients + `Disposition-Notification-To: true`).
5.  **Backend**:
    - `MailSendingService` constructs MIME message.
    - Sends via SMTP (Local).
    - Moves temp file to permanent Webfolder storage.
    - Returns Success.

### 4.2 Syncing Mail (List)
1.  **Frontend**: `GET /api/v1/mail/sync?since=123456`.
2.  **Backend**:
    - Checks IMAP folder state.
    - Returns "New Messages" + "Deleted UIDs".
3.  **Frontend**: Updates IndexedDB. Renders list from Local DB (Instant).

---
