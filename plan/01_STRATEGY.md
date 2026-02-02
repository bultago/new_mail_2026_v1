# Project Modernization Strategy

This document outlines the high-level strategy for migrating the legacy `TMS_WEBMAIL_746S` system to `new_mail_2026_v1`.

## 1. Core Principles
- **Incremental Migration (Strangler Fig Pattern)**: Do not rewrite everything at once. Pick one domain (e.g., Mail) and migrate it end-to-end.
- **TDD (Test Driven Development)**: No production code without a failing unit test. This is crucial for the legacy "untestable" code.
- **DDD (Domain Driven Design)**: Move business logic from Service/Action layers into **Domain Entities**.
- **Clean Architecture**: Separate Domain, Application (Service), and Infrastructure (Web/DB) layers.

## 2. Architecture Analysis (Current vs Target)

| Layer | As-Is (Legacy Hybrid) | To-Be (Modern Spring) | Action Item |
| :--- | :--- | :--- | :--- |
| **Presentation** | **Struts 2** Actions (`*.do`) + JSP | **Spring MVC** (`@RestController`) + Vue.js | Replace Actions with Controllers |
| **Business** | **Spring 2.5** Actions/Managers | **Spring 6** Services (POJO) | Refactor Actions to Services |
| **Persistence** | **MyBatis** (XML + DAO Support) | **MyBatis** (Mapper Interfaces) | Remove DAO Support classes |
| **Frontend** | JSP / jQuery / iframe | Vue 3 / Shadcn UI / Vite | Rewrite UI completely |

> **Critical Finding**: The system uses Struts 2 for routing but delegates to Spring Beans. We must sever this link by creating new Spring Controllers that call the refactored Services directly.

## 3. Migration Phases

### Phase 1: Preparation (Done)
- [x] Repository Initialization
- [x] History Merge (Frontend/Backend)
- [x] Deep Source Analysis (`06_FEATURE_LIST.md`)

### Phase 2: Backend Modernization (Ongoing)
**Focus**: Mail Domain (Sending/Reading) & Authentication
1.  **Isolate**: Identify the Struts Action (e.g., `sendMessageAction`).
2.  **Test**: Write a failing test for the desired Service capability.
3.  **Refactor**: Extract logic from `Action` bean into a pure `*Service` class.
    *   *Remove usage of `Map`, `JSONObject`, `HttpServletRequest` in logic.*
4.  **Expose**: Create a new `MailController` (`/api/v1/mail/send`).
5.  **Clean**: Verify the old `struts-mail.xml` entry can be deprecated.

### Phase 3: Hybrid IMAP Web Client Construction
> **Pivot**: Not just a UI rewrite, but a **Thick-Client Architecture** (Outlook-like).

1.  **Architecture**: `Vue.js` + `IndexedDB` (Local Store) + `Sync Engine`.
2.  **Dual Mode**:
    - **Terrace Mode**: Supports Webfolder, BigFile, Read Receipt (Proprietary).
    - **Standard Mode**: Compatible with 3rd party IMAP standards.
3.  **Backend Role**: Acts as Sync Provider & Feature Handler (BigFile Upload, MDN).
4.  **Reference**: See `plan/10_IMAP_WEB_CLIENT_ARCH.md`.

### Phase 4: Expansion
- Repeat Phase 2 & 3 for **Scheduler**, **AddressBook**, **Organization**, **Webfolder**.

## 4. Development Guidelines
- **Commit Message**: `[Feature/Refactor/Fix] Description`
- **File Location**:
    - Backend: `new_mail_2026_v1/api/src/main/java...`
    - Frontend: `new_mail_2026_v1/ui/src...`
    - Plans: `new_mail_2026_v1/plan/...`

---
**Reference**:
- Detailed Feature Map: `plan/06_FEATURE_LIST.md`
- Service Refactoring Guide: `plan/04_SERVICE_REFACTORING.md`
