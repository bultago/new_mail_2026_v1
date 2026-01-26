# Domain Model Extraction Plan

Extract rich domain models from legacy `Action` classes and `Map` based data structures.

## Todo List

### Mail Domain
- [ ] **Define `Mail` Aggregate Root**
  - Attributes: `messageId`, `subject`, `sender`, `recipients`, `sentDate`, `status`
  - Invariants: Logic for validating email addresses, max sizes, etc.
- [ ] **Define `Attachment` Entity**
  - Logic: File type validation, storage path resolution.
- [ ] **Define `MailBox` Value Object/Entity**
  - Logic: Folder structure, quota management.

### User/Auth Domain
- [ ] **Define `User` Entity**
  - Attributes: `userId`, `password` (hashed), `name`, `department`.
- [ ] **Define `Organization` Aggregate**
  - Logic: Hierarchy management.

### Scheduler Domain
- [ ] **Define `Schedule` Entity** (Priority: High, partially started)
  - recurring patterns, alarms.

## Actions
1. Search legacy code for `*VO.java` or `*Bean.java` to find existing data structures.
2. Convert them to Domain Entities with behaviors.
3. Remove dependency on `HttpServletRequest` or raw `Map` from these classes.
