plugins {
	java
	id("org.springframework.boot") version "4.1.2"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.themis"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

val jooqCodegen = configurations.dependencyScope("jooqCodegen")
val jooqCodegenClasspath = configurations.resolvable("jooqCodegenClasspath") {
	extendsFrom(jooqCodegen)
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("org.springframework.boot:spring-boot-starter-flyway")
	implementation("org.springframework.boot:spring-boot-starter-jooq")
	implementation("org.springframework.boot:spring-boot-starter-kafka")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-webmvc")
	implementation("org.flywaydb:flyway-database-postgresql")
	runtimeOnly("org.postgresql:postgresql")

	jooqCodegen("org.jooq:jooq-codegen")
	jooqCodegen("org.jooq:jooq-meta")
	jooqCodegen("org.postgresql:postgresql")

	testImplementation("org.springframework.boot:spring-boot-starter-flyway-test")
	testImplementation("org.springframework.boot:spring-boot-starter-jooq-test")
	testImplementation("org.springframework.boot:spring-boot-starter-kafka-test")
	testImplementation("org.springframework.boot:spring-boot-starter-validation-test")
	testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
	testImplementation("org.springframework.boot:spring-boot-testcontainers")
	testImplementation("org.testcontainers:testcontainers-junit-jupiter")
	testImplementation("org.testcontainers:testcontainers-kafka")
	testImplementation("org.testcontainers:testcontainers-postgresql")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

val jooqOutputDir = layout.buildDirectory.dir("generated-src/jooq/main")
sourceSets {
	main {
		java {
			srcDir(jooqOutputDir)
		}
	}
}

tasks.register<JavaExec>("generateJooq") {
	group = "jooq"
	description = "Generates type-safe jOOQ Java classes from the PostgreSQL schema"
	classpath = files(jooqCodegenClasspath)
	mainClass.set("org.jooq.codegen.GenerationTool")
	args("src/main/resources/jooq-config.xml")

	inputs.files(fileTree("src/main/resources/db/migration"))
	inputs.file("src/main/resources/jooq-config.xml")
	outputs.dir(jooqOutputDir)
}

tasks.named("compileJava") {
	dependsOn("generateJooq")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
