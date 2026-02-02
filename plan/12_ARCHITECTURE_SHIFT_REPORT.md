# Architecture Refactoring Report: AS-IS vs TO-BE

본 문서는 **Legacy Monolith** 구조에서 **Interface-based Dual Provider** 패턴으로의 아키텍처 변화를 기술합니다. 단순한 기능 이관이 아닌, 구조적 결합도를 낮추고 유연성을 확보한 기술적 진보에 초점을 맞춥니다.

## 1. Core Architecture Shift

### AS-IS: Tightly Coupled Monolith (강결합 구조)
기존 Legacy 시스템은 Controller(Action)가 특정 비즈니스 로직(Manager)과 프로토콜 구현체에 **직접적으로 의존**하는 구조였습니다.

*   **구조 특징**: 
    *   `Action` 클래스가 `Manager` 구현체를 직접 `new` 하거나 주입받음.
    *   특정 프로토콜(`Ladmin`, `TimsClient`) 코드가 비즈니스 로직에 혼재됨.
    *   **확장 불가**: 표준 IMAP이나 다른 구현체로 전환하려면 코드 전체를 수정해야 함.

```java
// [AS-IS] Legacy Code Example
public class FolderManageAction extends BaseAction {
    // ❌ 강한 결합: 특정 구현체(LadminManager)에 직접 의존
    private LadminManager ladminManager; 

    public String backup() {
        // ❌ 비즈니스 로직과 프로토콜 로직이 혼재
        ladminManager.sendCommand("USER_MAILBOX_BACKUP", ...);
        return "success";
    }
}
```

### TO-BE: Interface-based Dual Provider (유연한 전략 패턴)
새로운 시스템은 **Strategy Pattern**과 **Abstract Factory**를 도입하여, Controller가 구체적인 구현을 알지 못해도 동작하도록 설계되었습니다.

*   **구조 특징**:
    *   **Interface**: `MailServiceProvider`가 모든 메일 기능의 표준 명세(Contract)를 정의.
    *   **Factory**: `MailServiceFactory`가 설정(`nugenmail.provider`)에 따라 적절한 구현체를 런타임에 주입.
    *   **Implementation**: `StandardImapService`(표준)와 `TerraceImapService`(레거시 호환)가 각각 인터페이스를 구현.

```java
// [TO-BE] Modern Architecture Example
@RestController
@RequiredArgsConstructor
public class MailFolderController {
    // ✅ 느슨한 결합: 구체적인 구현 클래스(Standard/Terrace)를 모름
    private final MailServiceFactory mailServiceFactory;

    @PostMapping("/backup")
    public ApiResponse backup() {
        // ✅ 다형성(Polymorphism): 설정에 따라 Standard 또는 Terrace 로직이 자동 실행됨
        mailServiceFactory.getService().startFolderBackup(...);
        return ApiResponse.success();
    }
}
```

---

## 2. Component Comparison

| Layer | AS-IS (Legacy) | TO-BE (Modern) | Improvement |
| :--- | :--- | :--- | :--- |
| **API Entry** | Struts Action (`FolderManageAction`) | Spring RestController (`MailFolderController`) | 표준 Spring MVC, JSON 응답 |
| **Abstraction** | 없음 (None) | **`MailServiceProvider` Interface** | 구현체 교체 용이성 확보 (OCP 준수) |
| **Dispatch** | Hardcoded Dependency | **`MailServiceFactory`** | 설정 기반 런타임 동적 전환 가능 |
| **Backup Logic** | `LadminManager` (JNI/Native) | `StandardImapService` (Async Thread)<br>`TerraceImapService` (Socket Client) | 레거시 종속성 완전 제거 (Pure Java) |

## 3. Key Benefits (기대 효과)

1.  **Zero Legacy Dependency (레거시 종속성 제거)**
    *   기존에는 백업 기능을 고치려면 `ladmin.jar`나 `Action` 소스를 건드려야 했으나, 이제는 `StandardImapService`만 수정하면 표준 모드에서 즉시 반영됩니다.
    
2.  **Runtime Flexibility (런타임 유연성)**
    *   `application.yml` 설정 변경(`nugenmail.provider=standard` vs `legacy`)만으로 서버 재시작 없이(또는 최소한의 재시작으로) 동작 모드를 변경할 수 있습니다.
    
3.  **Testability (테스트 용이성)**
    *   인터페이스(`MailServiceProvider`)를 Mocking하여 컨트롤러 테스트가 가능해졌습니다. 더 이상 테스트를 위해 실제 Ladmin 서버를 띄울 필요가 없습니다.

## 4. Implementation Status
현재 **Dual Provider 구조 리팩토링이 완료**되었으며, 각 서비스의 백업 기능 구현도 완료되었습니다.

*   **Interface**: `MailServiceProvider` (Defined)
*   **Provider 1**: `StandardImapService` (Implemented Async Backup)
*   **Provider 2**: `TerraceImapService` (Implemented Socket Backup)
*   **Factory**: `MailServiceFactory` (Active)
