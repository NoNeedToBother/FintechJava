plugins {
    id("java")
    id("org.springframework.boot") version "3.3.4"
    jacoco
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

apply(plugin = "io.spring.dependency-management")

group = "ru.kpfu.itis.paramonov"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation(project(":time-logging-starter"))
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test:3.6.10")

    val testContainersVersion = "1.19.8"
    testImplementation("org.testcontainers:testcontainers:$testContainersVersion")
    testImplementation("org.testcontainers:junit-jupiter:$testContainersVersion")

    testImplementation("org.wiremock:wiremock-standalone:3.6.0")
    testImplementation("org.wiremock.integrations.testcontainers:wiremock-testcontainers-module:1.0-alpha-14")

    val lombokVersion = "1.18.32"
    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)

    classDirectories.setFrom(files(classDirectories.files.map {
        fileTree(it) {
            exclude(
                "ru/kpfu/itis/paramonov/dto/**"
            )
        }
    }))
}

