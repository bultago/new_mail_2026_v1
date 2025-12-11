# MyBatis queryForMap() → selectMap() 변환

## 작업 개요
- **작업 ID**: P2-010 (일부)
- **작업 일시**: 2025-10-17 14:00
- **목표**: MyBatis 3.x API로 메서드 변환

## 배경

iBATIS 2.x의 `queryForMap(statement, param, keyProperty, valueProperty)` 메서드가 MyBatis 3.x에는 존재하지 않습니다.

MyBatis 3.x에서는 `selectMap(statement, param, mapKey)` 형식으로 변경되었습니다.

## 변환 패턴

### iBATIS/기존 방식
```java
getSqlSession().queryForMap("statement", param, "key", "value")
```

### MyBatis 3.x 방식
```java
getSqlSession().selectMap("statement", param, "value")
```

**주요 차이점**:
- 메서드명: `queryForMap()` → `selectMap()`
- 파라미터: 4개 → 3개
- keyProperty는 SQL 매퍼 XML에서 `@MapKey` 어노테이션으로 지정

## 변환 작업

### 1. SystemConfigDao.java (5개 메서드)

**파일**: `src/com/terracetech/tims/webmail/common/dao/SystemConfigDao.java`

#### 1.1 getMailConfig()
```java
// Before
return getSqlSession().queryForMap("SystemConfig.readSystemConfig", configNames, "key", "value");

// After
return getSqlSession().selectMap("SystemConfig.readSystemConfig", configNames, "value");
```

#### 1.2 getDomainConfig()
```java
// Before
return getSqlSession().queryForMap("SystemConfig.readDomainSystemConfig", configParam, "key", "value");

// After
return getSqlSession().selectMap("SystemConfig.readDomainSystemConfig", configParam, "value");
```

#### 1.3 getWebAccessConfig()
```java
// Before
return (Map)getSqlSession().queryForMap("SystemConfig.readWebAccessConfig",null,"key","value");

// After
return (Map)getSqlSession().selectMap("SystemConfig.readWebAccessConfig",null,"value");
```

#### 1.4 getArchiveConfig()
```java
// Before
return getSqlSession().queryForMap("SystemConfig.readArchiveConfig",param, "key", "value");

// After
return getSqlSession().selectMap("SystemConfig.readArchiveConfig",param, "value");
```

#### 1.5 readConfigFile()
```java
// Before
return getSqlMapClient().queryForMap("SystemConfig.readConfigFile",configName,"key","value");

// After
return getSqlSession().selectMap("SystemConfig.readConfigFile",configName,"value");
```

### 2. MailUserDao.java (1개 메서드)

**파일**: `src/com/terracetech/tims/webmail/mailuser/dao/MailUserDao.java`

```java
// Before
Map<String, String> configMap = getSqlSession().queryForMap("MailUser.getUserSetting", param, "key", "value");

// After
Map<String, String> configMap = getSqlSession().selectMap("MailUser.getUserSetting", param, "value");
```

### 3. MailDomainDao.java (1개 메서드)

**파일**: `src/com/terracetech/tims/webmail/mailuser/dao/MailDomainDao.java`

```java
// Before
return getSqlSession().queryForMap("MailDomain.readLocalDomain",null,"key","value");

// After
return getSqlSession().selectMap("MailDomain.readLocalDomain",null,"value");
```

### 4. HybridMobileDao.java (1개 메서드)

**파일**: `src/com/terracetech/tims/hybrid/common/dao/HybridMobileDao.java`

```java
// Before
return (Map)getSqlSession().queryForMap("mobile.readUserMobileAccessKey",mailUserSeq,"key","value");

// After
return (Map)getSqlSession().selectMap("mobile.readUserMobileAccessKey",mailUserSeq,"value");
```

## 작업 통계

- **총 변환 파일**: 4개
- **총 변환 메서드**: 8개
- **변환 패턴**: queryForMap(4 params) → selectMap(3 params)

### 파일별 통계
| 파일 | 메서드 수 |
|------|----------|
| SystemConfigDao.java | 5개 |
| MailUserDao.java | 1개 |
| MailDomainDao.java | 1개 |
| HybridMobileDao.java | 1개 |

## 후속 작업 필요

### SQL 매퍼 XML 수정 필요
SQL 매퍼 XML 파일에서 `resultMap`의 key 속성을 명시해야 합니다:

```xml
<!-- 예시 -->
<select id="readSystemConfig" parameterType="list" resultType="map">
    SELECT config_key as key, config_value as value
    FROM system_config
    WHERE config_key IN
    <foreach item="item" collection="list" open="(" separator="," close=")">
        #{item}
    </foreach>
</select>
```

**주의**: MyBatis의 `selectMap()`은 결과의 특정 컬럼을 Map의 key로 사용합니다. 
세 번째 파라미터인 `"value"`는 Map의 key로 사용할 프로퍼티명입니다.

## 참고 자료

- [MyBatis 3 Migration Guide](https://mybatis.org/mybatis-3/migration.html)
- iBATIS 2.x queryForMap() deprecated
- MyBatis 3.x SqlSession API

## 검증 필요

컴파일은 성공하지만, 실행 시 동작 확인이 필요합니다:
1. SQL 매퍼 XML의 resultMap 설정 확인
2. Map key/value 구조 확인
3. 각 메서드 단위 테스트 수행

## 참고사항

이 변환은 코드 레벨의 변환이며, 실제 동작을 위해서는:
1. SQL 매퍼 XML의 MyBatis 3.x 형식 전환 필요
2. Spring 설정에서 SqlSessionFactory 설정 필요
3. 통합 테스트로 동작 검증 필요

