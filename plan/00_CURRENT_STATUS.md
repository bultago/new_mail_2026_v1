# Backend Implementation Status (Final Analysis)

**Architecture: Hybrid (Struts 2.5.33 + Spring 6.1.13 + Java 21)**

## 1. Critical Finding
- **Controller Layer**: **None** (Struts `web.xml` deleted). 
  - *Note*: `pom.xml` still has Struts 2.5.33 dependency, but it is **Dead Code/Traces** only.
- **Service Layer**: **Spring 6.1.13** (Upgraded in Oct 2025 Phase 4).
- **Status**: The libraries are modern, but the **architecture** is still "Action-based" (Legacy Style).
- **Build Status**: **Broken** (348 Errors) due to incomplete migration.

## 2. Component Mapping (As-Is -> To-Be)
| Domain | Struts URL (`*.do`) | Spring Bean (Legacy Action) | Modern Goal (`@Service`) |
| :--- | :--- | :--- | :--- |
| **Mail** | `/mail/send.do` | `sendMessageAction` | `MailSendingService` |
| | `/mail/list.do` | `listMessageAction` | `MailListingService` |
| **Addr** | `/addr/list.do` | `viewAddressMemberAction` | `ContactService` |
| **Sched**| `/calendar/month.do`| `monthSchedulerAction` | `SchedulerService` |

## 3. Action Items
1.  **Stop writing Actions**.
2.  **Extract Logic**: Move code from `*Action.java` to `*Service.java` (POJO).
3.  **New API**: Expose `*Service` via `@RestController` (Spring MVC 6).

## 4. Legacy Migration Status (Found Oct 2025)
> **Detected from `api/docs/works`**: A previous migration attempt (Phase 4) was partially executed.

- **Last Status**: **Build Broken** (348 Compile Errors).
- **Completed Work**:
    - **Package Cleanup**: `samsung`, `mcnc` packages removed.
    - **VO Creation**: `SchedulerVO`, `UserInfoVO` etc. created.
    - **DAO Updates**: Method signatures updated in `PrivateAddressBookDao`, etc.
    - **Annotations**: `@Service`, `@Transactional` added to some Managers.
- **Action Item**: We must fix the 348 errors before proceeding with new Refactoring.

## 5. Reference Repositories (Legacy Source)
> **NOTE**: Do not build these. Use them to understand logic (e.g., Encoding, Proprietary IMAP).

1.  **JavaMail (Custom)**: `d:\workspace\javamail_ko`
    - Custom handling for Korean charset/encodings.
2.  **Terrace Libraries**: `d:\workspace\TMS_WEBMAIL_746_ORIGINAL_LIB\src`
    - `com.terracetech.tims.mail` -> (Logic for `terrace_mailapi`)
    - `com.terracetech.tims.common` -> (Logic for `terrace_common`)
