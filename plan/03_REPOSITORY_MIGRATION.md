# Repository & Database Migration Plan

Migrate legacy `db-config/*.xml` (iBatis/Legacy) to Modern MyBatis 3.x Mappers.

## Legacy Status
- Location: `api/web/WEB-INF/classes/db-config/`
- DB Support: MySQL, Oracle, Derby (Need to standardize or abstraction)

## Todo List

### Core Mail Mappers (Priority: High)
- [ ] **`letterMap.xml` / `letterService.xml` -> `MailMapper` hierarchy**
  - Migrate `selectMailList`, `selectMailDetail`
- [ ] **`mailBoxMap.xml` -> `MailBoxMapper`**
- [ ] **`mailAttachMap.xml` -> `AttachmentMapper`**

### Address Book
- [ ] **`addrbookMap.xml` / `addrbook.xml` -> `AddressBookMapper`**

### System
- [ ] **`systemConfig.xml` -> `SystemConfigMapper.java`**

## Policy
1. Create Interface in `infrastructure/persistence/mapper`.
2. Move XML to `src/main/resources/mapper/`.
3. Remove raw SQL strings if found in Java code.
4. **Test**: Write `@MybatisTest` for each converted mapper.
