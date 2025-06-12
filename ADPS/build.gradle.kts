plugins {
	java
	id("org.springframework.boot") version "3.5.0"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(22)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	runtimeOnly("org.postgresql:postgresql")
	implementation("org.projectlombok:lombok:1.18.34")
	implementation("org.springframework.content:spring-content-fs-boot-starter:1.2.0")
	implementation("org.springframework:spring-async")
	implementation("com.github.paulcwarren:spring-content-fs-boot-starter:1.2.6")
	implementation("com.github.paulcwarren:spring-content-jpa-boot-starter:1.2.6")
	implementation ("org.apache.poi:poi-ooxml:5.2.3")
	implementation ("org.apache.pdfbox:pdfbox:2.0.28")
	implementation ("org.apache.poi:poi:5.2.3")




	implementation("org.apache.tika:tika-core:2.4.1")
	implementation("org.apache.poi:poi:5.2.2")
	implementation("org.apache.pdfbox:pdfbox:2.0.26")


	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
