plugins {
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.20"
    kotlin("plugin.spring") version "1.9.20"
    kotlin("plugin.jpa") version "1.9.20"
}

dependencies {
    // ===== Core Spring =====
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework:spring-aspects")

    // ===== Database & Storage =====
    implementation("org.postgresql:postgresql:42.6.0")
    implementation("org.hibernate:hibernate-core:6.3.1.Final") // Для Large Object
    implementation("org.liquibase:liquibase-core:4.25.0")

    // ===== Document Processing =====
    implementation("org.apache.tika:tika-core:2.9.1")
    implementation("com.github.librepdf:openpdf:1.3.30") // PDF
    implementation("org.apache.poi:poi-ooxml:5.2.5") // DOCX

    // ===== Utilities & Kotlin =====
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.3")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    // ===== Caching =====
    implementation("com.github.ben-manes.caffeine:caffeine:3.1.8")

    // ===== Testing =====
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("io.mockk:mockk:1.13.8")
}

// ===== Конфигурация JVM =====
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(22))
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}