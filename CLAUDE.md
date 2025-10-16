# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Communication Guidelines

**Language Policy:**
- Always respond in Korean (한국어) unless explicitly requested otherwise
- Technical terms may be kept in English when appropriate
- Code comments and documentation should follow the existing language style in files

**Claude Global Policies:**
- Follow all Anthropic Claude usage policies and ethical guidelines
- Refuse requests for malicious code, security exploits, or harmful operations
- Prioritize defensive security tasks only (analysis, detection, protection)
- Never assist with credential harvesting, unauthorized access, or malicious activities
- Maintain professional objectivity and technical accuracy over user validation

## Project Overview

TIMS7 Webmail v7.4.6S - Enterprise Java webmail system built with Struts 2, Spring Framework, and iBATIS. This is a legacy backup codebase requiring careful handling.

## Critical Safety Rules

**NEVER use these commands without explicit user approval:**
- `rm -rf` - file deletion
- `awk`, `sed` - text processing (use Edit tool instead)
- Overwriting files without backup
- Modifying `web/WEB-INF/web.xml`
- Copying individual class files (use JAR builds)
- Any work on 2-factor authentication

**Required workflow:**
1. Always get user approval before critical operations
2. Create backups before file modifications
3. Use Playwright instead of curl for testing
4. Wait for user instructions - do not proceed independently
5. When told "혼자 판단하지마" (don't decide alone), stop immediately and request approval

## Build System

This project uses Apache Ant for building:

**Primary build command:**
```bash
ant compile
```

**Clean and rebuild:**
```bash
ant clean compile
```

**Build with JavaDoc:**
```bash
ant dist
```

**JavaScript minification:**
```bash
ant js.minify
```

### Build Process Details

The build system:
1. Runs `js.minify` target to concatenate and minify JavaScript files
2. Compiles Java sources from `src/` to `build/WEB-INF/classes/`
3. Copies resources (properties files) to build directory
4. Uses YUI Compressor for JavaScript optimization

**Key directories:**
- `src/` - Java source code
- `web/` - Web application resources (JSP, static files)
- `build/` - Compiled output
- `serverlib/was/` - WAS (Tomcat) libraries
- `serverlib/etc/` - Additional dependencies
- `web/WEB-INF/lib/` - Application libraries

## Architecture

### Framework Stack

**Core frameworks:**
- Struts 2 (web MVC framework)
- Spring Framework 2.5 (IoC, dependency injection)
- iBATIS SqlMapClient (ORM/data access)
- DWR (Direct Web Remoting for AJAX)

**Servlet container:**
- Apache Tomcat 4+ compatible
- Configured via `web/WEB-INF/web.xml`

### Package Structure

```
com.terracetech.tims.webmail/
├── mail/                    # Email functionality
│   ├── manager/            # Business logic (MailManager, LetterManager, Pop3Manager)
│   │   └── send/          # Send handlers (Strategy pattern)
│   ├── dao/               # Database access
│   ├── vo/                # Value objects
│   └── action/            # Struts2 actions
├── mailuser/              # User management
│   ├── manager/           # UserAuthManager, MailUserManager
│   └── sso/               # SSO integration
├── scheduler/             # Calendar/scheduling
├── organization/          # Organization structure
├── webfolder/            # Web folder management
├── setting/              # User settings
├── home/                 # Home/dashboard
└── common/               # Shared utilities
    ├── WebmailAccessFilter
    └── ResponseHeaderFilter
```

### Design Patterns

**Strategy Pattern for Mail Sending:**
- `SendMessageDirector` - coordinates sending
- `SendHandler` interface with implementations:
  - `NormalSendHandler` - standard email
  - `BatchSendHandler` - bulk sending
  - `ReservedSendHandler` - scheduled sending
  - `EachSendHandler` - individual processing

**Layered Architecture:**
```
Action (Struts2) → Manager (Business Logic) → DAO (Data Access) → Database
                    ↓
                   VO (Value Objects)
```

### Spring Configuration

Module-specific Spring configuration files in `web/WEB-INF/classes/web-config/`:
- `spring-mail.xml` - Mail module beans
- `spring-login.xml` - Authentication
- `spring-addr.xml` - Address book
- `spring-calendar.xml` - Calendar/scheduler
- `spring-setting.xml` - User settings
- `spring-bbs.xml` - Bulletin board
- `spring-webfolder.xml` - Web folder
- `spring-organization.xml` - Organization
- `spring-mobile.xml`, `spring-jmobile.xml` - Mobile interfaces
- `spring-note.xml` - Notes

All configurations use `default-autowire="byName"` for dependency injection.

### Data Access

**iBATIS SqlMapClient:**
- DAO classes extend base patterns
- SQL mappings in XML files
- Transaction management via Spring

**Example DAOs:**
- `BigAttachDao` - Large attachment handling
- `LetterDao` - Email message persistence
- `CacheEmailDao` - Email caching

### Web Layer

**Filters (execution order):**
1. `WebmailAccessFilter` - Access control
2. `StrutsPrepareAndExecuteFilter` - Struts2 processing
3. `ResponseHeaderFilter` - Cache control for static resources
4. `ResponseHeaderDwrFilter` - Cache control for DWR/JS

**Key directories:**
- `web/js/` - JavaScript libraries (jQuery 1.3.2, Prototype)
- `web/design/` - CSS and images
- `web/classic/` - Classic UI templates
- `web/mobile/` - Mobile interface
- `web/editor/` - Rich text editor

## Internationalization

Message resources in `web/WEB-INF/classes/messageResources/`:
- `*_ko.properties` - Korean
- `*_jp.properties` - Japanese
- `*.properties` - English (default)

Categories:
- `MailMessage` - Email UI strings
- `CommonMessage` - Shared strings
- `SchedulerMessage` - Calendar strings
- `SettingMessage` - Settings UI
- `AddrApplicationResources` - Address book

## Development Guidelines

### Code Modification Rules

**From .cursorrules:**
1. Never simplify or use default values to bypass errors
2. Fix compilation errors with correct method signatures
3. Never use fully-qualified class names in code (use imports)
4. Avoid explicit casting with package names
5. Never leave commented-out "temporary" implementations
6. No batch script operations for code changes
7. Individual files must be analyzed for correct API usage

### Testing

**Playwright test scripts:**
- Store in `/opt/TMS_WEBMAIL_746S_ORIGINAL_BACKUP_20250629_063747/test-scripts/`
- Execute from that directory
- Save results and screenshots there

### Security Components

**Available in codebase:**
- `INISAFEMail_v1.4.0.jar` - Email security
- `Xecure7.jar` - Security framework
- `INICrypto_v3.2.1_signed.jar` - Cryptography
- Secure mail handling in `com.terracetech.secure/` package

### JavaMail Integration

Email operations use JavaMail API:
- IMAP/POP3 for receiving
- SMTP for sending
- Folder management via `FolderHandler`
- Message parsing via `MessageParser`

## Important Notes

1. This is a backup of a production system - treat as read-only unless explicitly authorized
2. Project uses outdated libraries (jQuery 1.3.2, Spring 2.5) - security considerations apply
3. Multi-language support (Korean, Japanese, English)
4. Both desktop and mobile interfaces included
5. Integrated with SSO systems
6. Complex mail handling with attachments, scheduling, and batch operations
