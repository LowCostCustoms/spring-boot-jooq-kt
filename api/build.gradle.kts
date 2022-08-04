plugins {
    id("org.springframework.boot") version "2.7.2"
    id("io.spring.dependency-management") version "1.0.12.RELEASE"
    kotlin("plugin.spring") version "1.6.21"
}

group = "com.lowcostcustoms.plyaground.spring.jooq"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

val jooqVersion by extra("3.16.6")

dependencies {
    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    // Jooq
    implementation(project(":jooq"))
    implementation("org.jooq:jooq:$jooqVersion")
    implementation("org.jooq:jooq-meta-extensions:$jooqVersion")

    // Logging
    implementation("io.github.microutils:kotlin-logging-jvm:2.1.20")

    // Flyway
    implementation("org.flywaydb:flyway-core:8.5.11")

    // SpringDoc
    implementation("org.springdoc:springdoc-openapi-webflux-core:1.6.9")
    implementation("org.springdoc:springdoc-openapi-webflux-ui:1.6.9")
    implementation("org.springdoc:springdoc-openapi-kotlin:1.6.9")

    // Postgres
    implementation("org.postgresql:r2dbc-postgresql")
    runtimeOnly("org.postgresql:postgresql")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
}
