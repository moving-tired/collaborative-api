import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI

group = "org.tired"
version = "1.0.0-SNAPSHOT"

plugins {
    val kotlinVersion = "1.3.61"
    id("org.jetbrains.kotlin.plugin.spring") version kotlinVersion
    id("org.springframework.boot") version "2.2.4.RELEASE"
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
    application
}

repositories {
    maven { setUrl("http://oss.jfrog.org/artifactory/oss-snapshot-local") }
}

configure<DependencyManagementExtension> {
    imports { mavenBom("org.springframework.boot:spring-boot-dependencies:2.2.4.RELEASE") }
}

val swaggerDepVersion = "3.0.0-SNAPSHOT"

dependencies {
    implementation(project(":core"))
    implementation(project(":infrastructure"))
    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib-jdk8"))
    implementation("redis.clients:jedis:3.1.0")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.vault:spring-vault-core:2.1.3.RELEASE")
    implementation("org.codehaus.janino:janino:3.0.15")
    implementation("net.logstash.logback:logstash-logback-encoder:6.2")

    // For Api Rest Validations
    implementation("javax.validation:validation-api:2.0.1.Final")
    implementation("org.hibernate.validator:hibernate-validator:6.0.17.Final")
    implementation("org.hibernate.validator:hibernate-validator-annotation-processor:6.0.17.Final")
    implementation("javax.el:javax.el-api:3.0.0")
    implementation("javax.inject:javax.inject:1")

    // For swagger
    implementation("io.springfox:springfox-swagger-ui:$swaggerDepVersion")
    implementation("io.springfox:springfox-swagger2:$swaggerDepVersion")
    implementation("io.springfox:springfox-spring-integration-webmvc:$swaggerDepVersion")
    implementation("org.springframework.integration:spring-integration-http:5.1.7.RELEASE")
    implementation("io.springfox:springfox-spring-webmvc:$swaggerDepVersion")
    implementation("io.springfox:springfox-bean-validators:$swaggerDepVersion")

    testImplementation(project(":core").dependencyProject.sourceSets.test.get().output)
    testImplementation("org.springframework.boot:spring-boot-starter-webflux") {
        exclude(module = "junit")
    }
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(module = "junit")
    }
}

application {
    mainClassName = "org.tired"
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.test {
    useJUnitPlatform {
        includeEngines("junit-jupiter")
    }
    testLogging {
        events("passed", "skipped", "failed")
    }
}
