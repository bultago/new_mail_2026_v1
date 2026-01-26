# Backend Implementation Status (Final Analysis)

**Architecture: Hybrid (Struts 2 Views + Spring 2.5 Logic)**

## 1. Critical Finding
- **Controller Layer**: **Struts 2** (`struts-mail.xml` etc.) handles Request/Response.
- **Service Layer**: **Spring 2.5** (`spring-mail.xml`) manages Action/Manager Beans.
- **Problem**: Business logic is tightly coupled to `Action` classes (Legacy), making it hard to test or reuse.

## 2. Component Mapping (As-Is -> To-Be)
| Domain | Struts URL (`*.do`) | Spring Bean (Legacy) | Modern Goal (`@Service`) |
| :--- | :--- | :--- | :--- |
| **Mail** | `/mail/send.do` | `sendMessageAction` | `MailSendingService` |
| | `/mail/list.do` | `listMessageAction` | `MailListingService` |
| **Addr** | `/addr/list.do` | `viewAddressMemberAction` | `ContactService` |
| **Sched**| `/calendar/month.do`| `monthSchedulerAction` | `SchedulerService` |

## 3. Action Items
1.  **Stop writing Actions**.
2.  **Extract Logic**: Move code from `*Action.java` to `*Service.java` (POJO).
3.  **New API**: Expose `*Service` via `@RestController` (Spring MVC 6).
