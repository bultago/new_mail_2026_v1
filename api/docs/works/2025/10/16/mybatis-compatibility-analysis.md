# iBATIS 2.3 → MyBatis 3.x API 차이 분석

**분석일**: 2025-10-16  
**작업 ID**: P1-026  
**대상**: iBATIS 2.3.4 → MyBatis 3.5.13

---

## 현재 iBATIS 사용 현황

### iBATIS 버전
- **현재**: iBATIS 2.3.4.726 (2010년 중단)
- **목표**: MyBatis 3.5.13 (현재 최신 안정 버전)

### 사용 현황 (분석 결과)
- SQL 매핑 파일: 82개
- SQL 쿼리: ~2,412개
- 지원 DB: MySQL, PostgreSQL, Oracle, Derby (4개)

---

## API 변환 매핑 테이블

### 1. 설정 파일

| iBATIS | MyBatis |
|--------|---------|
| `SqlMapConfig.xml` | `mybatis-config.xml` |
| `<sqlMap>` | `<mapper>` |
| `<sqlMapConfig>` | `<configuration>` |

### 2. 네임스페이스

| iBATIS | MyBatis |
|--------|---------|
| `namespace="Mail"` (문자열) | `namespace="com.xx.mapper.MailMapper"` (인터페이스 경로) |

### 3. 파라미터 표기

| iBATIS | MyBatis | 비고 |
|--------|---------|------|
| `#userId#` | `#{userId}` | 파라미터 바인딩 |
| `$userId$` | `${userId}` | 문자열 치환 |

### 4. 결과 매핑

| iBATIS | MyBatis |
|--------|---------|
| `resultClass` | `resultType` |
| `parameterClass` | `parameterType` |

### 5. Java API

| iBATIS | MyBatis | 비고 |
|--------|---------|------|
| `SqlMapClient` | `SqlSession` | 핵심 인터페이스 |
| `SqlMapClientTemplate` | `SqlSessionTemplate` | Spring 통합 |
| `queryForList()` | `selectList()` | 목록 조회 |
| `queryForObject()` | `selectOne()` | 단건 조회 |
| `insert()` | `insert()` | 동일 |
| `update()` | `update()` | 동일 |
| `delete()` | `delete()` | 동일 |

### 6. DAO 패턴

#### iBATIS DAO (현재)
```java
public class MailDao extends SqlMapClientDaoSupport {
    
    public List<MailVO> selectMailList(String userId) {
        return (List<MailVO>) getSqlMapClientTemplate()
                .queryForList("Mail.selectMailList", userId);
    }
}
```

#### MyBatis Mapper (목표)
```java
@Mapper
public interface MailMapper {
    List<MailVO> selectMailList(@Param("userId") String userId);
}
```

**장점**:
- 구현체 불필요 (MyBatis가 자동 생성)
- 타입 안정성 향상
- 보일러플레이트 코드 제거

---

## SQL 매핑 XML 변환

### 변환 규칙

#### 1. DTD 선언
```xml
<!-- iBATIS -->
<!DOCTYPE sqlMap PUBLIC "-//ibatis.apache.org//DTD SQL Map 2.0//EN"      
    "http://ibatis.apache.org/dtd/sql-map-2.dtd">

<!-- MyBatis -->
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
    "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
```

#### 2. 루트 엘리먼트
```xml
<!-- iBATIS -->
<sqlMap namespace="MailUser">
</sqlMap>

<!-- MyBatis -->
<mapper namespace="com.terracetech.tims.webmail.mailuser.mapper.MailUserMapper">
</mapper>
```

#### 3. SELECT 쿼리
```xml
<!-- iBATIS -->
<select id="selectMailList" resultClass="com.terracetech.tims.webmail.mail.vo.MailVO">
    SELECT * FROM tb_mail WHERE user_id = #userId#
</select>

<!-- MyBatis -->
<select id="selectMailList" resultType="com.terracetech.tims.webmail.mail.vo.MailVO">
    SELECT * FROM tb_mail WHERE user_id = #{userId}
</select>
```

#### 4. INSERT 쿼리
```xml
<!-- iBATIS -->
<insert id="insertMail" parameterClass="MailVO">
    INSERT INTO tb_mail VALUES (#mailId#, #subject#, #sender#)
</insert>

<!-- MyBatis -->
<insert id="insertMail" parameterType="MailVO" useGeneratedKeys="true" keyProperty="mailId">
    INSERT INTO tb_mail VALUES (#{mailId}, #{subject}, #{sender})
</insert>
```

---

## 자동 변환 스크립트 요구사항

### 변환 대상
- 82개 SQL 매핑 XML 파일
- 4개 DB별 파일 (총 82개)

### 변환 항목
1. DTD 선언 변경
2. `<sqlMap>` → `<mapper>`
3. `#param#` → `#{param}`
4. `$param$` → `${param}`
5. `resultClass` → `resultType`
6. `parameterClass` → `parameterType`
7. namespace 속성 → 인터페이스 경로 (수동 확인 필요)

### 자동 변환 불가능 (수동 작업)
- namespace 경로 변경 (인터페이스 전체 경로로)
- SQL 문법 차이 (DB별 확인)
- ResultMap 복잡한 매핑

---

## Spring 통합 변경

### iBATIS-Spring (현재)
```xml
<bean id="sqlMapClient" class="org.springframework.orm.ibatis.SqlMapClientFactoryBean">
    <property name="dataSource" ref="dataSource"/>
    <property name="configLocation" value="classpath:SqlMapConfig.xml"/>
</bean>

<bean id="mailDao" class="com.terracetech.tims.webmail.mail.dao.MailDao">
    <property name="sqlMapClient" ref="sqlMapClient"/>
</bean>
```

### MyBatis-Spring (목표)
```xml
<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
    <property name="dataSource" ref="dataSource"/>
    <property name="configLocation" value="classpath:mybatis-config.xml"/>
    <property name="mapperLocations" value="classpath*:**/mapper/*.xml"/>
</bean>

<bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
    <property name="basePackage" value="com.terracetech.tims.webmail.**.mapper"/>
    <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"/>
</bean>
```

---

## 마이그레이션 전략

### Phase 3 작업 순서

1. **MyBatis 설정** (P3-001 ~ P3-005)
   - mybatis-config.xml 생성
   - SqlSessionFactory Bean 설정
   - 자동 변환 스크립트 작성

2. **모듈별 순차 전환**
   - 공통 모듈 (P3-006 ~ P3-008)
   - 독립 모듈 (P3-009 ~ P3-012)
   - 보조 모듈 (P3-013 ~ P3-016)
   - 핵심 모듈 (P3-017 ~ P3-020)

3. **iBATIS 제거** (P3-021 ~ P3-028)
   - 라이브러리 제거
   - import 제거
   - 테스트 및 검증

---

## 예상 작업량

### SQL 매핑 파일 변환
```
82개 파일 × 평균 2시간 = 164시간 (약 20일)

자동 변환 스크립트 사용 시:
82개 파일 × 평균 30분 = 41시간 (약 5일)
```

### Mapper 인터페이스 생성
```
82개 Mapper 인터페이스 × 평균 1시간 = 82시간 (약 10일)
```

### Manager 클래스 업데이트
```
약 50개 Manager × 평균 30분 = 25시간 (약 3일)
```

**총 예상**: 약 18일 (자동화 활용 시)

---

## 테스트 전략

### 1. Mapper 단위 테스트
```java
@SpringJUnitConfig
@Transactional
class MailMapperTest {
    
    @Autowired
    private MailMapper mailMapper;
    
    @Test
    void testSelectMailList() {
        List<MailVO> list = mailMapper.selectMailList("test@example.com");
        assertNotNull(list);
    }
}
```

### 2. SQL 실행 검증
- 모든 SQL 쿼리 실행 확인
- 결과 매핑 정확성 검증
- 성능 비교 (iBATIS vs MyBatis)

---

## 완료 조건

- ✅ API 변환 매핑 테이블 작성
- ✅ SQL 매핑 XML 변환 규칙 정의
- ✅ DAO → Mapper 전환 패턴 수립
- ✅ 자동 변환 스크립트 요구사항 정의
- ✅ 테스트 전략 수립

---

**결론**: iBATIS → MyBatis 전환은 **명확한 변환 규칙**이 있어 안전하게 진행 가능. 자동 변환 스크립트 개발이 핵심.

**작성자**: Backend 개발자 + DBA  
**완료일**: 2025-10-16

