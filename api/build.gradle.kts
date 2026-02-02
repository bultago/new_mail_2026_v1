plugins {
    id("java")
    id("war")
    id("org.springframework.boot") version "3.3.0"
    id("io.spring.dependency-management") version "1.1.5"
}

group = "com.nugenmail"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring MVC Core
    implementation("org.springframework.boot:spring-boot-starter-web:3.3.0")
    implementation("org.springframework:spring-context-support:6.1.11")
    implementation("jakarta.servlet:jakarta.servlet-api:6.0.0")

    // Security
    implementation("org.springframework.security:spring-security-web:6.3.1")
    implementation("org.springframework.security:spring-security-config:6.3.1")

    // Data
    implementation("org.mybatis:mybatis:3.5.16")
    implementation("org.mybatis:mybatis-spring:3.0.3")
    implementation("org.springframework:spring-jdbc:6.1.11")
    implementation("mysql:mysql-connector-java:5.1.49")

    // Mail
    implementation("jakarta.mail:jakarta.mail-api:2.1.3")
    implementation("org.eclipse.angus:angus-mail:2.0.3")

    // Utilities
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.17.2")
    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")
    
    implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:3.0.3")
    
    implementation("org.slf4j:slf4j-api:2.0.13")
    implementation("ch.qos.logback:logback-classic:1.5.6")
    
    // Crypto Support
    implementation("org.bouncycastle:bcprov-jdk15on:1.70")
    implementation("commons-codec:commons-codec:1.16.0")

    // JWT
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

    // Annotation
    implementation("jakarta.annotation:jakarta.annotation-api:2.1.1")

    // Testing
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.3")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.3")
    testImplementation("org.springframework:spring-test:6.1.11")
    testImplementation("org.mockito:mockito-core:5.11.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

task<JavaExec>("runDecryptTest") {
    mainClass.set("com.nugenmail.util.DecryptTest")
    classpath = sourceSets["main"].runtimeClasspath
}

task<JavaExec>("runSchemaInspector") {
    mainClass.set("com.nugenmail.util.SchemaInspector")
    classpath = sourceSets["main"].runtimeClasspath
}


