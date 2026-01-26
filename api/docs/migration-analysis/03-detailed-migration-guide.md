# 상세 마이그레이션 실행 가이드

## 문서 정보
- **작성일**: 2025-10-14
- **대상**: 개발팀
- **목적**: 실제 마이그레이션 작업 수행을 위한 상세 가이드

---

## 1. 작업 전 필수 준비사항

### 1.1 백업 체크리스트
```bash
# 1. 전체 소스 백업
cd /opt
tar -czf TMS_WEBMAIL_746S_BACKUP_$(date +%Y%m%d_%H%M%S).tar.gz TMS_WEBMAIL_746S_ORIGINAL/

# 2. 데이터베이스 백업
mysqldump -u username -p database_name > db_backup_$(date +%Y%m%d).sql
# 또는 PostgreSQL
pg_dump -U username database_name > db_backup_$(date +%Y%m%d).sql

# 3. 설정 파일 별도 백업
mkdir -p backups/config
cp -r web/WEB-INF/*.xml backups/config/
cp -r web/WEB-INF/classes/web-config/*.xml backups/config/

# 4. Git 태그 생성
git tag -a v7.4.6S-pre-migration -m "Before Spring MVC migration"
git push origin v7.4.6S-pre-migration
```

### 1.2 개발 환경 요구사항
```
필수 소프트웨어:
- Java JDK 11 이상 (권장: 11 LTS)
- Apache Tomcat 9.x
- Maven 3.6+ 또는 Gradle 7.x+
- IDE: IntelliJ IDEA 또는 Eclipse with Spring Tools
- Git 2.x+

선택 사항:
- Docker (컨테이너화)
- Jenkins (CI/CD)
- SonarQube (코드 품질)
```

---

## 2. Phase 2 상세: Spring 5.x 업그레이드

### 2.1 의존성 변경 (Maven 예시)

#### 2.1.1 pom.xml 생성
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.terracetech</groupId>
    <artifactId>tims-webmail</artifactId>
    <version>7.4.6S-SNAPSHOT</version>
    <packaging>war</packaging>

    <properties>
        <java.version>11</java.version>
        <spring.version>5.3.30</spring.version>
        <maven.compiler.source>11</maven.compiler.source>
        <maven.compiler.target>11</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <!-- Spring Framework -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>${spring.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-web</artifactId>
            <version>${spring.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-webmvc</artifactId>
            <version>${spring.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-jdbc</artifactId>
            <version>${spring.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-tx</artifactId>
            <version>${spring.version}</version>
        </dependency>

        <!-- Struts 2 (Phase 4까지 유지) -->
        <dependency>
            <groupId>org.apache.struts</groupId>
            <artifactId>struts2-core</artifactId>
            <version>2.3.37</version> <!-- 보안 패치 버전 -->
        </dependency>
        <dependency>
            <groupId>org.apache.struts</groupId>
            <artifactId>struts2-spring-plugin</artifactId>
            <version>2.3.37</version>
        </dependency>

        <!-- Servlet API -->
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>javax.servlet-api</artifactId>
            <version>4.0.1</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>javax.servlet.jsp</groupId>
            <artifactId>javax.servlet.jsp-api</artifactId>
            <version>2.3.3</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>jstl</artifactId>
            <version>1.2</version>
        </dependency>

        <!-- Logging -->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>1.7.36</version>
        </dependency>
        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
            <version>1.2.11</version>
        </dependency>

        <!-- 기타 기존 라이브러리들 유지 -->
    </dependencies>

    <build>
        <finalName>tims-webmail</finalName>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.10.1</version>
                <configuration>
                    <source>11</source>
                    <target>11</target>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-war-plugin</artifactId>
                <version>3.3.2</version>
                <configuration>
                    <warSourceDirectory>web</warSourceDirectory>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

#### 2.1.2 빌드 및 테스트
```bash
# Maven 빌드
mvn clean compile

# 컴파일 에러 확인
mvn compile 2>&1 | grep -i error

# WAR 패키징
mvn package

# 테스트 실행
mvn test
```

### 2.2 Spring 설정 파일 업데이트

#### 2.2.1 spring-mail.xml 업데이트 예시
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="
           http://www.springframework.org/schema/beans
           http://www.springframework.org/schema/beans/spring-beans.xsd
           http://www.springframework.org/schema/context
           http://www.springframework.org/schema/context/spring-context.xsd
           http:///www.springframework.org/schema/tx
           http://www.springframework.org/schema/tx/spring-tx.xsd">

    <!-- Component Scan -->
    <context:component-scan base-package="com.terracetech.tims.webmail.mail" />

    <!-- Property Placeholder -->
    <context:property-placeholder location="classpath:application.properties" />

    <!-- Transaction Manager -->
    <bean id="transactionManager"
          class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource"/>
    </bean>

    <!-- Annotation-driven Transactions -->
    <tx:annotation-driven transaction-manager="transactionManager"/>

    <!-- 기존 Bean 정의들 유지 -->
    <bean id="mailManager" class="com.terracetech.tims.webmail.mail.manager.MailManager"/>
    <!-- ... -->
</beans>
```

#### 2.2.2 자동 스캔 전환 (선택사항)
```java
// @Service 어노테이션 추가
@Service
public class MailManager {
    // XML에서 Bean 정의 제거 가능
}

// @Repository 어노테이션 추가
@Repository
public class MailDao extends SqlMapClientDaoSupport {
    // ...
}
```

### 2.3 컴파일 에러 수정

#### 2.3.1 Deprecated API 교체
```java
// 예시: SimpleDateFormat → DateTimeFormatter
// 변경 전
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
String dateStr = sdf.format(new Date());

// 변경 후
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
String dateStr = LocalDate.now().format(formatter);
```

#### 2.3.2 제네릭 타입 경고 수정
```java
// 변경 전
List mailList = mailDao.selectMailList(userId);

// 변경 후
List<MailVO> mailList = mailDao.selectMailList(userId);
```

---

## 3. Phase 3 상세: MyBatis 마이그레이션

### 3.1 MyBatis 설정

#### 3.1.1 mybatis-config.xml 생성
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <settings>
        <!-- iBATIS 호환성 설정 -->
        <setting name="useGeneratedKeys" value="true"/>
        <setting name="mapUnderscoreToCamelCase" value="true"/>
        <setting name="cacheEnabled" value="true"/>
        <setting name="lazyLoadingEnabled" value="false"/>
        <setting name="aggressiveLazyLoading" value="false"/>
        <setting name="multipleResultSetsEnabled" value="true"/>
        <setting name="useColumnLabel" value="true"/>
        <setting name="defaultExecutorType" value="SIMPLE"/>
        <setting name="defaultStatementTimeout" value="25000"/>
    </settings>

    <typeAliases>
        <!-- 자주 사용하는 VO 별칭 등록 -->
        <typeAlias type="com.terracetech.tims.webmail.mail.vo.MailVO" alias="MailVO"/>
        <typeAlias type="com.terracetech.tims.webmail.mailuser.vo.UserVO" alias="UserVO"/>
        <!-- 또는 패키지 스캔 -->
        <package name="com.terracetech.tims.webmail.mail.vo"/>
    </typeAliases>
</configuration>
```

#### 3.1.2 Spring 설정에 MyBatis 등록
```xml
<!-- spring-common.xml -->
<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
    <property name="dataSource" ref="dataSource"/>
    <property name="configLocation" value="classpath:mybatis-config.xml"/>
    <property name="mapperLocations" value="classpath*:com/terracetech/tims/webmail/**/mapper/*.xml"/>
</bean>

<!-- Mapper 스캔 -->
<bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
    <property name="basePackage" value="com.terracetech.tims.webmail"/>
    <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"/>
    <property name="annotationClass" value="org.apache.ibatis.annotations.Mapper"/>
</bean>
```

### 3.2 SQL 매핑 파일 변환 도구

#### 3.2.1 자동 변환 스크립트 (Python)
```python
#!/usr/bin/env python3
"""
iBATIS XML → MyBatis XML 변환 스크립트
"""

import re
import os
import sys

def convert_ibatis_to_mybatis(ibatis_xml):
    """iBATIS XML을 MyBatis XML로 변환"""

    mybatis_xml = ibatis_xml

    # 1. sqlMap → mapper
    mybatis_xml = mybatis_xml.replace('<sqlMap', '<mapper')
    mybatis_xml = mybatis_xml.replace('</sqlMap>', '</mapper>')

    # 2. namespace 속성 → 인터페이스 경로로 변경 필요 (수동)
    # namespace="Mail" → namespace="com.terracetech.tims.webmail.mail.mapper.MailMapper"

    # 3. 파라미터 표기: #param# → #{param}
    mybatis_xml = re.sub(r'#(\w+)#', r'#{\1}', mybatis_xml)

    # 4. 파라미터 표기: $param$ → ${param}
    mybatis_xml = re.sub(r'\$(\w+)\$', r'${\1}', mybatis_xml)

    # 5. resultClass → resultType
    mybatis_xml = mybatis_xml.replace('resultClass=', 'resultType=')

    # 6. parameterClass → parameterType
    mybatis_xml = mybatis_xml.replace('parameterClass=', 'parameterType=')

    # 7. DTD 변경
    mybatis_xml = mybatis_xml.replace(
        '<!DOCTYPE sqlMap PUBLIC "-//iBATIS.com//DTD SQL Map 2.0//EN" '
        '"http://www.ibatis.com/dtd/sql-map-2.dtd">',
        '<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" '
        '"http://mybatis.org/dtd/mybatis-3-mapper.dtd">'
    )

    return mybatis_xml

def main():
    if len(sys.argv) < 2:
        print("Usage: python convert_ibatis.py <ibatis_xml_file>")
        sys.exit(1)

    input_file = sys.argv[1]
    output_file = input_file.replace('.xml', '_mybatis.xml')

    with open(input_file, 'r', encoding='utf-8') as f:
        ibatis_content = f.read()

    mybatis_content = convert_ibatis_to_mybatis(ibatis_content)

    with open(output_file, 'w', encoding='utf-8') as f:
        f.write(mybatis_content)

    print(f"Converted: {input_file} → {output_file}")
    print("주의: namespace는 수동으로 확인 및 수정이 필요합니다.")

if __name__ == '__main__':
    main()
```

**사용법**:
```bash
python convert_ibatis.py web/WEB-INF/classes/sqlmap/Mail-sqlmap.xml
```

### 3.3 Mapper 인터페이스 생성

#### 3.3.1 DAO → Mapper 변환 예시
```java
// ===== 기존: MailDao.java =====
package com.terracetech.tims.webmail.mail.dao;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import java.util.List;

public class MailDao extends SqlMapClientDaoSupport {

    public List<MailVO> selectMailList(String userId) {
        return (List<MailVO>) getSqlMapClientTemplate()
                .queryForList("Mail.selectMailList", userId);
    }

    public MailVO selectMail(String mailId) {
        return (MailVO) getSqlMapClientTemplate()
                .queryForObject("Mail.selectMail", mailId);
    }

    public int insertMail(MailVO mail) {
        return (Integer) getSqlMapClientTemplate()
                .insert("Mail.insertMail", mail);
    }

    public int updateMail(MailVO mail) {
        return getSqlMapClientTemplate()
                .update("Mail.updateMail", mail);
    }

    public int deleteMail(String mailId) {
        return getSqlMapClientTemplate()
                .delete("Mail.deleteMail", mailId);
    }
}


// ===== 신규: MailMapper.java =====
package com.terracetech.tims.webmail.mail.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface MailMapper {

    List<MailVO> selectMailList(@Param("userId") String userId);

    MailVO selectMail(@Param("mailId") String mailId);

    int insertMail(MailVO mail);

    int updateMail(MailVO mail);

    int deleteMail(@Param("mailId") String mailId);
}
```

#### 3.3.2 MailMapper.xml 생성
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="com.terracetech.tims.webmail.mail.mapper.MailMapper">

    <!-- Result Map 정의 -->
    <resultMap id="mailResultMap" type="MailVO">
        <id property="mailId" column="mail_id"/>
        <result property="userId" column="user_id"/>
        <result property="subject" column="subject"/>
        <result property="sender" column="sender"/>
        <result property="regDate" column="reg_date"/>
    </resultMap>

    <!-- 메일 목록 조회 -->
    <select id="selectMailList" resultMap="mailResultMap">
        SELECT mail_id, user_id, subject, sender, reg_date
        FROM tb_mail
        WHERE user_id = #{userId}
        ORDER BY reg_date DESC
    </select>

    <!-- 메일 상세 조회 -->
    <select id="selectMail" resultMap="mailResultMap">
        SELECT mail_id, user_id, subject, sender, content, reg_date
        FROM tb_mail
        WHERE mail_id = #{mailId}
    </select>

    <!-- 메일 등록 -->
    <insert id="insertMail" parameterType="MailVO" useGeneratedKeys="true" keyProperty="mailId">
        INSERT INTO tb_mail (user_id, subject, sender, content, reg_date)
        VALUES (#{userId}, #{subject}, #{sender}, #{content}, NOW())
    </insert>

    <!-- 메일 수정 -->
    <update id="updateMail" parameterType="MailVO">
        UPDATE tb_mail
        SET subject = #{subject},
            content = #{content},
            upd_date = NOW()
        WHERE mail_id = #{mailId}
    </update>

    <!-- 메일 삭제 -->
    <delete id="deleteMail">
        DELETE FROM tb_mail
        WHERE mail_id = #{mailId}
    </delete>

</mapper>
```

### 3.4 Manager/Service 클래스 업데이트
```java
// MailManager.java 수정
@Service
@Transactional
public class MailManager {

    // DAO → Mapper 변경
    private final MailMapper mailMapper;

    @Autowired
    public MailManager(MailMapper mailMapper) {
        this.mailMapper = mailMapper;
    }

    public List<MailVO> getMailList(String userId) {
        return mailMapper.selectMailList(userId);
    }

    public MailVO getMail(String mailId) {
        return mailMapper.selectMail(mailId);
    }

    @Transactional
    public int sendMail(MailVO mail) {
        return mailMapper.insertMail(mail);
    }
}
```

### 3.5 테스트 작성
```java
@SpringJUnitConfig(locations = {"classpath:spring-test-config.xml"})
@Transactional
class MailMapperTest {

    @Autowired
    private MailMapper mailMapper;

    @Test
    void testSelectMailList() {
        List<MailVO> mailList = mailMapper.selectMailList("test@example.com");
        assertNotNull(mailList);
        assertTrue(mailList.size() > 0);
    }

    @Test
    void testInsertMail() {
        MailVO mail = new MailVO();
        mail.setUserId("test@example.com");
        mail.setSubject("Test Mail");
        mail.setSender("sender@example.com");
        mail.setContent("Test Content");

        int result = mailMapper.insertMail(mail);
        assertEquals(1, result);
        assertNotNull(mail.getMailId()); // auto-generated key
    }
}
```

---

## 4. Phase 4 상세: Spring MVC 전환

### 4.1 Controller 작성 패턴

#### 4.1.1 기본 Controller 구조
```java
package com.terracetech.tims.webmail.mail.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/mail")
public class MailController {

    private final MailManager mailManager;

    @Autowired
    public MailController(MailManager mailManager) {
        this.mailManager = mailManager;
    }

    /**
     * 메일 목록 조회
     */
    @GetMapping("/list")
    public String list(@SessionAttribute("user") UserVO user,
                      @RequestParam(defaultValue = "1") int page,
                      Model model) {

        List<MailVO> mailList = mailManager.getMailList(user.getUserId(), page);
        model.addAttribute("mailList", mailList);
        model.addAttribute("currentPage", page);

        return "mail/list"; // → /WEB-INF/jsp/mail/list.jsp
    }

    /**
     * 메일 읽기
     */
    @GetMapping("/read/{mailId}")
    public String read(@PathVariable String mailId, Model model) {

        MailVO mail = mailManager.getMail(mailId);
        if (mail == null) {
            throw new MailNotFoundException("메일을 찾을 수 없습니다.");
        }

        model.addAttribute("mail", mail);
        return "mail/read";
    }

    /**
     * 메일 작성 폼
     */
    @GetMapping("/compose")
    public String composeForm(Model model) {
        model.addAttribute("mailForm", new MailForm());
        return "mail/compose";
    }

    /**
     * 메일 전송
     */
    @PostMapping("/send")
    public String send(@Valid @ModelAttribute MailForm mailForm,
                      BindingResult bindingResult,
                      @SessionAttribute("user") UserVO user,
                      RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "mail/compose";
        }

        try {
            mailManager.sendMail(mailForm, user);
            redirectAttributes.addFlashAttribute("message", "메일이 전송되었습니다.");
            return "redirect:/mail/list";

        } catch (Exception e) {
            bindingResult.reject("sendFailed", "메일 전송에 실패했습니다.");
            return "mail/compose";
        }
    }

    /**
     * 메일 삭제
     */
    @PostMapping("/delete")
    public String delete(@RequestParam String[] mailIds,
                        RedirectAttributes redirectAttributes) {

        mailManager.deleteM ails(mailIds);
        redirectAttributes.addFlashAttribute("message",
                mailIds.length + "개의 메일이 삭제되었습니다.");

        return "redirect:/mail/list";
    }

    /**
     * AJAX: 메일 읽음 처리
     */
    @PostMapping("/markAsRead")
    @ResponseBody
    public Map<String, Object> markAsRead(@RequestParam String mailId) {

        Map<String, Object> result = new HashMap<>();
        try {
            mailManager.markAsRead(mailId);
            result.put("success", true);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }

        return result;
    }
}
```

#### 4.1.2 Form 객체 작성
```java
package com.terracetech.tims.webmail.mail.form;

import javax.validation.constraints.*;

public class MailForm {

    @NotEmpty(message = "받는사람을 입력하세요")
    @Email(message = "올바른 이메일 형식이 아닙니다")
    private String to;

    private String cc;

    private String bcc;

    @NotEmpty(message = "제목을 입력하세요")
    @Size(max = 200, message = "제목은 200자를 초과할 수 없습니다")
    private String subject;

    private String content;

    private List<MultipartFile> attachments;

    // Getters/Setters
}
```

### 4.2 JSP 뷰 전환

#### 4.2.1 메일 목록 JSP
```jsp
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><spring:message code="mail.list.title"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/design/css/mail.css">
</head>
<body>

<h1><spring:message code="mail.list.title"/></h1>

<!-- 메시지 표시 -->
<c:if test="${not empty message}">
    <div class="alert alert-success">
        <c:out value="${message}"/>
    </div>
</c:if>

<!-- 메일 목록 -->
<form action="${pageContext.request.contextPath}/mail/delete" method="post">
    <table class="mail-list">
        <thead>
            <tr>
                <th><input type="checkbox" id="checkAll"></th>
                <th><spring:message code="mail.sender"/></th>
                <th><spring:message code="mail.subject"/></th>
                <th><spring:message code="mail.date"/></th>
            </tr>
        </thead>
        <tbody>
            <c:choose>
                <c:when test="${empty mailList}">
                    <tr>
                        <td colspan="4"><spring:message code="mail.list.empty"/></td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach items="${mailList}" var="mail">
                        <tr class="${mail.readFlag ? 'read' : 'unread'}">
                            <td>
                                <input type="checkbox" name="mailIds" value="${mail.mailId}">
                            </td>
                            <td><c:out value="${mail.sender}"/></td>
                            <td>
                                <a href="${pageContext.request.contextPath}/mail/read/${mail.mailId}">
                                    <c:out value="${mail.subject}"/>
                                </a>
                            </td>
                            <td>
                                <fmt:formatDate value="${mail.regDate}" pattern="yyyy-MM-dd HH:mm"/>
                            </td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </tbody>
    </table>

    <div class="actions">
        <button type="submit"><spring:message code="mail.delete"/></button>
        <a href="${pageContext.request.contextPath}/mail/compose" class="btn">
            <spring:message code="mail.compose"/>
        </a>
    </div>
</form>

<!-- 페이징 -->
<div class="pagination">
    <c:if test="${currentPage > 1}">
        <a href="?page=${currentPage - 1}">&laquo; 이전</a>
    </c:if>
    <span>Page ${currentPage}</span>
    <a href="?page=${currentPage + 1}">다음 &raquo;</a>
</div>

<script src="${pageContext.request.contextPath}/js/jquery-3.6.0.min.js"></script>
<script>
$(document).ready(function() {
    // 전체 선택
    $('#checkAll').click(function() {
        $('input[name="mailIds"]').prop('checked', this.checked);
    });

    // 삭제 확인
    $('form').submit(function() {
        var checked = $('input[name="mailIds"]:checked').length;
        if (checked === 0) {
            alert('삭제할 메일을 선택하세요.');
            return false;
        }
        return confirm(checked + '개의 메일을 삭제하시겠습니까?');
    });
});
</script>

</body>
</html>
```

#### 4.2.2 메일 작성 JSP
```jsp
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><spring:message code="mail.compose.title"/></title>
</head>
<body>

<h1><spring:message code="mail.compose.title"/></h1>

<form:form action="${pageContext.request.contextPath}/mail/send"
           method="post"
           modelAttribute="mailForm"
           enctype="multipart/form-data">

    <div class="form-group">
        <label for="to"><spring:message code="mail.to"/>*</label>
        <form:input path="to" id="to" cssClass="form-control" required="true"/>
        <form:errors path="to" cssClass="error"/>
    </div>

    <div class="form-group">
        <label for="cc"><spring:message code="mail.cc"/></label>
        <form:input path="cc" id="cc" cssClass="form-control"/>
    </div>

    <div class="form-group">
        <label for="subject"><spring:message code="mail.subject"/>*</label>
        <form:input path="subject" id="subject" cssClass="form-control" required="true"/>
        <form:errors path="subject" cssClass="error"/>
    </div>

    <div class="form-group">
        <label for="content"><spring:message code="mail.content"/></label>
        <form:textarea path="content" id="content" rows="15" cssClass="form-control"/>
    </div>

    <div class="form-group">
        <label for="attachments"><spring:message code="mail.attachments"/></label>
        <input type="file" name="attachments" multiple id="attachments"/>
    </div>

    <div class="actions">
        <button type="submit" class="btn btn-primary">
            <spring:message code="mail.send"/>
        </button>
        <a href="${pageContext.request.contextPath}/mail/list" class="btn">
            <spring:message code="common.cancel"/>
        </a>
    </div>

</form:form>

</body>
</html>
```

### 4.3 예외 처리
```java
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MailNotFoundException.class)
    public ModelAndView handleMailNotFound(MailNotFoundException ex) {
        ModelAndView mav = new ModelAndView("error/404");
        mav.addObject("message", ex.getMessage());
        return mav;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception ex, HttpServletRequest request) {
        logger.error("Unhandled exception", ex);

        ModelAndView mav = new ModelAndView("error/error");
        mav.addObject("exception", ex);
        mav.addObject("url", request.getRequestURL());
        return mav;
    }
}
```

### 4.4 파일 업로드 처리
```java
@PostMapping("/send")
public String send(@Valid @ModelAttribute MailForm mailForm,
                  BindingResult bindingResult,
                  @RequestParam("attachments") MultipartFile[] files) {

    if (bindingResult.hasErrors()) {
        return "mail/compose";
    }

    // 첨부파일 처리
    List<AttachmentVO> attachments = new ArrayList<>();
    for (MultipartFile file : files) {
        if (!file.isEmpty()) {
            String savedPath = attachmentService.save(file);
            AttachmentVO attachment = new AttachmentVO();
            attachment.setOriginalName(file.getOriginalFilename());
            attachment.setSavedPath(savedPath);
            attachment.setFileSize(file.getSize());
            attachments.add(attachment);
        }
    }

    mailForm.setAttachments(attachments);
    mailManager.sendMail(mailForm);

    return "redirect:/mail/list";
}
```

---

## 5. 테스트 실행

### 5.1 단위 테스트
```java
@SpringJUnitConfig
@WebAppConfiguration
class MailControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    void testMailList() throws Exception {
        mockMvc.perform(get("/mail/list")
                        .sessionAttr("user", createTestUser()))
                .andExpect(status().isOk())
                .andExpect(view().name("mail/list"))
                .andExpect(model().attributeExists("mailList"));
    }

    @Test
    void testSendMail() throws Exception {
        mockMvc.perform(post("/mail/send")
                        .param("to", "test@example.com")
                        .param("subject", "Test Subject")
                        .param("content", "Test Content")
                        .sessionAttr("user", createTestUser()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/mail/list"));
    }
}
```

### 5.2 통합 테스트
```bash
# Maven으로 통합 테스트 실행
mvn verify

# Playwright E2E 테스트
npx playwright test
```

---

## 6. 배포 절차

### 6.1 WAR 파일 생성
```bash
# Maven 빌드
mvn clean package

# WAR 파일 확인
ls -lh target/tims-webmail.war
```

### 6.2 Tomcat 배포
```bash
# 1. 기존 애플리케이션 중지
$CATALINA_HOME/bin/shutdown.sh

# 2. 기존 WAR 백업
cp $CATALINA_HOME/webapps/tims-webmail.war backups/

# 3. 기존 디렉토리 제거
rm -rf $CATALINA_HOME/webapps/tims-webmail
rm $CATALINA_HOME/webapps/tims-webmail.war

# 4. 신규 WAR 배포
cp target/tims-webmail.war $CATALINA_HOME/webapps/

# 5. Tomcat 시작
$CATALINA_HOME/bin/startup.sh

# 6. 로그 모니터링
tail -f $CATALINA_HOME/logs/catalina.out
```

### 6.3 배포 후 검증
```bash
# 헬스 체크
curl http://localhost:8080/tims-webmail/health

# 주요 페이지 확인
curl -I http://localhost:8080/tims-webmail/login
curl -I http://localhost:8080/tims-webmail/mail/list
```

---

## 7. 롤백 절차

### 7.1 애플리케이션 롤백
```bash
# 1. Tomcat 중지
$CATALINA_HOME/bin/shutdown.sh

# 2. 현재 버전 제거
rm -rf $CATALINA_HOME/webapps/tims-webmail
rm $CATALINA_HOME/webapps/tims-webmail.war

# 3. 백업 버전 복원
cp backups/tims-webmail.war $CATALINA_HOME/webapps/

# 4. Tomcat 시작
$CATALINA_HOME/bin/startup.sh
```

### 7.2 데이터베이스 롤백
```bash
# MySQL
mysql -u username -p database_name < db_backup_20251014.sql

# PostgreSQL
psql -U username -d database_name -f db_backup_20251014.sql
```

---

## 8. 트러블슈팅

### 8.1 일반적인 문제들

#### 문제 1: ClassNotFoundException
```
원인: 라이브러리 의존성 누락
해결: pom.xml 확인 및 mvn dependency:tree 실행
```

#### 문제 2: No qualifying bean of type found
```
원인: Spring Bean 등록 누락
해결: @Component, @Service, @Repository 어노테이션 확인
      또는 component-scan 패키지 경로 확인
```

#### 문제 3: Could not resolve placeholder
```
원인: properties 파일 로드 실패
해결: application.properties 위치 및 PropertyPlaceholderConfigurer 설정 확인
```

#### 문제 4: HTTP 404 on JSP
```
원인: ViewResolver 설정 오류 또는 JSP 경로 불일치
해결: InternalResourceViewResolver의 prefix/suffix 확인
      실제 JSP 파일 경로 확인
```

### 8.2 로그 확인
```bash
# Tomcat 로그
tail -f $CATALINA_HOME/logs/catalina.out

# 애플리케이션 로그 (logback 설정에 따라)
tail -f /var/log/tims-webmail/application.log

# Spring 디버그 로깅 활성화 (logback.xml)
<logger name="org.springframework" level="DEBUG"/>
<logger name="com.terracetech.tims.webmail" level="DEBUG"/>
```

---

## 다음 문서
- `04-migration-checklist.md` - 단계별 체크리스트
- `05-testing-guide.md` - 테스트 가이드 (생성 예정)
