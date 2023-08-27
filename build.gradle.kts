import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.0"
    kotlin("plugin.spring") version "1.9.0"
    id("org.springframework.boot") version "2.7.0"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    application
    id("org.liquibase.gradle") version "2.0.4"
}
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

group = "org.ivcode"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")

    // Spring
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    //implementation("org.springframework.boot:spring-boot-starter-security")

    // Docker
    implementation("com.github.docker-java:docker-java:3.3.2")

    // H2
    implementation("com.h2database:h2")

    // Liquibase
    liquibaseRuntime("org.liquibase:liquibase-core:4.2.2")
    liquibaseRuntime("org.yaml:snakeyaml:1.17")
    liquibaseRuntime("info.picocli:picocli:4.6.1")
    liquibaseRuntime("com.h2database:h2")

    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.0.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}

liquibase {
    activities.register("main") {
        arguments = mapOf(
            "changeLogFile" to "./src/main/db/db.changelog.yml",
            "url" to "jdbc:h2:./h2/test",
            "username" to "userName",
            "password" to "secret",
            "logLevel" to "info"
        )
    }
}
