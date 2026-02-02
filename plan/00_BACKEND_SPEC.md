# Project Nugenmail: Backend Technical Specification

## 1. Project Overview
*   **Project Name**: Nugenmail (`com.nugenmail`)
*   **Artifact ID**: `nugenmail-api`
*   **Description**: A modern, clean-slate Spring Boot backend for the Webmail system ("New Mail 2026").
*   **Core Philosophy**: **Zero Dependencies on Legacy Code**. The system will be built from scratch using modern standards, interfacing with the legacy system *only* at the database level.

## 2. Technology Stack
| Component | Choice | Justification |
| :--- | :--- | :--- |
| **Language** | Java 21 (LTS) | Latest LTS features (Records, Pattern Matching, Virtual Threads readiness). |
| **Framework** | Spring MVC (Spring Framework 6.1.11) | Standard Spring MVC architecture (Non-Boot). |
| **Build Tool** | Gradle (Kotlin DSL) | Modern, type-safe build configuration. |
| **Persistence** | MyBatis 3.5.16+ | Compatibility with complex legacy SQL schemas while supporting modern Java types. |
| **Database** | MySQL / Oracle | Connectivity to existing legacy databases. |
| **Security** | Spring Security 6.3 | JWT-based Stateless Authentication. |
| **Mail** | Jakarta Mail 2.1 | Modern replacement for javax.mail. |
| **Testing** | JUnit 5, Mockito | Standard modern testing stack. |

## 3. Architecture Guidelines
The project follows a standard **Layered Architecture** with strict separation of concerns.

### 3.1. Package Structure
```
d:/workspace_nextgen/TMS_WEBMAIL_746S_ORIGINAL/new_mail_2026_v1/api/src/main/java/com/nugenmail
├── NugenmailApplication.java  # Main Entry Point
├── config/                    # Configuration Classes (Security, Swagger, WebMvc)
├── common/                    # Shared Utilities, Constants, Global Exceptions
├── domain/                    # Business Domains
│   ├── auth/                  # Authentication Module (Login, JWT)
│   ├── mail/                  # Mail Module (SMTP Sending, IMAP Listing)
│   └── user/                  # User Management (Read-Only from Legacy DB)
└── infra/                     # Infrastructure implementations
```

### 3.2. Legacy Integration Policy (Strict)
1.  **NO Legacy Source Code**: Do not copy/paste `com.terracetech` or `com.daou` classes.
2.  **NO Legacy Binaries**: Do not put `web/WEB-INF/lib/*.jar` into the classpath unless absolutely necessary for JDBC drivers.
3.  **Data Reuse Only**: We read/write to the *same database tables*, but we define our own **Entity** and **Mapper** classes.
4.  **No "Dual" Classpath**: The project must compile 100% independently of the legacy project.

## 4. Key Functional Modules (MVP)
1.  **Authentication**:
    *   Validate user credentials against legacy `TB_USER` (or equivalent) table.
    *   Issue JWT Access/Refresh functionality.
2.  **Mail Sending**:
    *   Use `spring-boot-starter-mail`.
    *   Implement SMTP sending logic from scratch.
3.  **Mail Listing**:
    *   Use `jakarta.mail` (IMAP) to fetch folder structures and message headers.
    *   Do NOT use legacy `MailSummaryDAO` if IMAP is the target source of truth.

## 5. Build & Environment
*   **JDK**: Eclipse Adoptium OpenJDK 21
*   **Gradle Wrapper**: Must be installed (version 8.8+ recommended).
