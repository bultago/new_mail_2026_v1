# Service Layer Refactoring Plan (Spring Legacy -> DDD)

Refactor existing legacy Spring Services (`com.terracetech.tims...service.*` or `manager.*`) to adhere to TDD/DDD principles.

## Current Issues
- **Anemic Domain Model**: Services handle all logic; Entities (`VO`) are dumb data containers.
- **Testability**: Heavy use of `new` keyword and static methods blocks Unit Testing.
- **Type Safety**: Excessive use of `Map`, `JSONObject`, `JSONArray`.

## Todo List

### Mail Features
- [ ] **Refactor `MailService` / `MailManager`**
  - Extract `Mail` Domain Entity logic.
  - Introduce pure DTOs instead of `Map`.
- [ ] **Refactor `SchedulerManager`**
  - Dependency Injection for `SchedulerHandler`.
  - Remove direct JSON dependency in business logic.

### Authentication
- [ ] **Refactor `LoginAction` -> `AuthenticationService`**
  - Logic: JWT generation, Password validation.

## Policy
1. Identify all `Action` classes in legacy source.
2. Isolate side-effects (HTTP, Database).
3. Move logic to `application` layer classes.
4. **TDD**: Write test for Service BEFORE moving the code.
