# Report: Legacy Code Migration & Build Restoration Strategy
**Status**: DRAFT
**Date**: 2026-01-28

## 1. Executive Summary
The compilation failures are caused by a fundamental incompatibility between the legacy **Struts 2.5.33** framework (which relies on the `javax.*` namespace) and the modern **Spring Boot 3** environment (which requires the `jakarta.*` namespace).

Simply "reverting" code to use `javax.*` imports (the previous attempt) creates an unresolvable "Dual Namespace" conflict where legacy libraries cannot interact with the new container's objects.

**To resolve this and comply with the directive to modernize, we must UPGRADE the legacy dependencies, not downgrade the code.**

## 2. Root Cause Analysis
*   **Struts 2.5.33 Dependency**: This version is hard-coded to use `javax.servlet.*`.
*   **Spring Boot 3 Container**: Provides `jakarta.servlet.*`.
*   **The Conflict**: When `BaseAction` implements `ServletRequestAware` (from Struts), it expects `setServletRequest(javax.servlet.http.HttpServletRequest)`. However, the container passes a `jakarta.servlet.http.HttpServletRequest`. These are different Java types, causing "Incompatible Type" errors that cannot be bridged without a library upgrade.
*   **Mail API Conflict**: Similar issues exist between `javax.mail` (Legacy/JTNEF) and `jakarta.mail` (Spring Boot 3).

## 3. Recommended Action Plan

### Phase 1: Dependency Modernization (Immediate)
We will modify `build.gradle.kts` to replace legacy libraries with their Jakarta-compatible equivalents.

| Legacy Library | Current Version | Target Version | Reason |
| :--- | :--- | :--- | :--- |
| **Struts 2 Core** | 2.5.33 | **6.3.0** | Native Jakarta EE support. |
| **JavaMail** | 1.6.2 | **Jakarta Mail 2.1.3** | Standard for Spring 6. |
| **JTNEF** | 2.0.0 | **TBD / Fork** | May require a Jakarta-compatible alternative or source patch.* |

*> Note: If JTNEF does not have a Jakarta version, we may need to decompile/source-patch it or strictly isolate usage.*

### Phase 2: Codebase Refactoring
Once dependencies are upgraded, we will systematically refactor the source code to align with the new libraries. "Reverting" will be abandoned.

1.  **Global Import Replacement**: Replace all `javax.servlet` and `javax.mail` imports with `jakarta.servlet` and `jakarta.mail`.
2.  **API Adaptation**:
    *   **Struts Actions**: Update `BaseAction` and all child actions to use Struts 6 APIs.
    *   **Mail Code**: Refactor `TMailPart`, `TMailMessage` to use `Angus Mail` (Jakarta impl) classes instead of `com.sun.mail`.
    *   **TNEF Handling**: Rewrite `extractTnefContent` to work with the Jakarta Mail API.

### Phase 3: Legacy Binary Libraries (`terrace_*.jar`)
The `web/WEB-INF/lib` directory contains proprietary jars (`terrace_mailapi.jar`, etc.).
*   **Step**: Attempt to compile WITHOUT these jars, relying on the `src` folder (since `com.terracetech.tims` seems to be in source).
*   **Risk**: If these jars contain closed-source components dependent on `javax`, they will break. If source is available, we rebuild them. If not, they are blocker.

## 4. Next Steps for Approval
1.  **Approve Struts Upgrade**: Confirm moving to Struts 6.3.0.
2.  **Approve Full Rewrite**: Authorize bulk changes from `javax` -> `jakarta` across 500+ files.

**Recommendation**: Proceed immediately with **Phase 1 (Upgrade Struts)** and **Phase 2 (Refactor)**.
