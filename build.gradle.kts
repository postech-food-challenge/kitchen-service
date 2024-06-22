val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val koin_version: String by project
val mockkVersion: String by project

plugins {
    kotlin("jvm") version "1.9.23"
    id("org.sonarqube") version "4.4.1.3373"
    id("io.ktor.plugin") version "2.3.9"
    id("jacoco")
}

group = "br.com.fiap.postech"
version = "0.0.1"

application {
    mainClass.set("br.com.fiap.postech.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

sonar {
    properties {
        property("sonar.projectKey", "postech-food-challenge_kitchen-ms")
        property("sonar.organization", "postech-food-challenge")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}

repositories {
    mavenCentral()
}

dependencies {
    //  KTOR
    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-netty")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization:$ktor_version")
    implementation("io.ktor:ktor-server-status-pages:$ktor_version")
    implementation("io.ktor:ktor-serialization-jackson:$ktor_version")
    //  KOIN
    implementation("io.insert-koin:koin-ktor:$koin_version")
    //  JACKSON
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.16.0")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.16.0")
    implementation("org.json:json:20240303")
    //  LOG
    implementation("ch.qos.logback:logback-classic:$logback_version")
    // COCUMBER
    implementation("io.cucumber:cucumber-java:7.17.0")
    testImplementation("io.cucumber:cucumber-junit:7.17.0")
    testImplementation("org.assertj:assertj-core:3.25.3")
    // TEST
    testImplementation("io.mockk:mockk:${mockkVersion}")
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")
    testImplementation("io.insert-koin:koin-test:3.2.0")
    // DYNAMODB
    implementation("aws.sdk.kotlin:dynamodb:1.2.38")
    // REST CLIENT
    implementation("io.ktor:ktor-client-core:2.3.11")
    implementation("io.ktor:ktor-client-cio:2.3.11")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")

}

jacoco {
    toolVersion = "0.8.12"
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)
    }
    classDirectories.setFrom(
        files(classDirectories.files.map {
            fileTree(it) {
                exclude(
                    "br/com/fiap/postech/domain/**",
                    "br/com/fiap/postech/configuration/**",
                    "br/com/fiap/postech/infraestucture/**"
                )
            }
        })
    )
}
