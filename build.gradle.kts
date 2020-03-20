import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType
import org.owasp.dependencycheck.reporting.ReportGenerator

val junitVersion = "5.5.1"
val junitPlatformVersion = "1.5.1"
val jacksonVersion = "2.10.0.pr2"
val newrelicVersion = "5.9.0"
val micrometerVersion = "1.3.2"
val jacocoModuleFilePath = "jacoco/test.exec"

buildscript {
    extra.set("awsSDKVersion", "1.11.670")
    extra.set("micrometerVersion", "1.3.2")
    extra.set("jacksonVersion", "2.10.0.pr2")
}

extra.set("springBootVersion", "2.2.4.RELEASE")

plugins {
    val kotlinVersion = "1.3.61"
    base
    kotlin("jvm") version kotlinVersion apply false
    id("org.jlleitschuh.gradle.ktlint").version("9.1.1")
    id("org.jlleitschuh.gradle.ktlint-idea").version("9.1.1")
    id("org.owasp.dependencycheck") version "5.2.4"
    id("io.gitlab.arturbosch.detekt").version("1.0.0-RC16")
    id("org.sonarqube") version "2.8"
    maven
    jacoco
    idea
    java
}

val newrelic by configurations.creating {
    extendsFrom(configurations["implementation"])
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "jacoco")
    apply(plugin = "idea")
    apply(plugin = "maven")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "org.jlleitschuh.gradle.ktlint-idea")
    apply(plugin = "org.owasp.dependencycheck")
    apply(plugin = "io.gitlab.arturbosch.detekt")
    apply(plugin = "org.sonarqube")

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "1.8"
        }
    }

    dependencies {
        testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
        testImplementation("org.junit.jupiter:junit-jupiter-params:$junitVersion")
        testImplementation("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
        testImplementation("org.junit.platform:junit-platform-commons:$junitPlatformVersion")
        testImplementation("org.junit.platform:junit-platform-engine:$junitPlatformVersion")
        testImplementation("org.junit.platform:junit-platform-launcher:$junitPlatformVersion")
        implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")
        implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
        implementation("com.fasterxml.jackson.core:jackson-core:$jacksonVersion")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
        implementation("io.micrometer:micrometer-core:$micrometerVersion")
        implementation("io.micrometer:micrometer-registry-statsd:$micrometerVersion")
        implementation("com.google.guava:guava:28.1-jre")
        implementation("org.slf4j:slf4j-api:1.7.30")
        testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.1.0")
        testImplementation("org.mockito:mockito-junit-jupiter:2.23.0")
        testImplementation("com.willowtreeapps.assertk:assertk-jvm:0.19")
    }

    tasks.test {
        useJUnitPlatform {
            filter {
                excludeTags("integration-test")
            }
            includeEngines("junit-jupiter")
        }
        testLogging {
            events("passed", "skipped", "failed")
        }
    }

    detekt {
        input = files(rootProject.rootDir)
        config = files("$rootDir/detekt-config.yml")
        parallel = true
        ignoreFailures = false
    }

    sonarqube {
        properties {
            property("sonar.language", "kotlin")
            property("sonar.sources", "src/main")
            property("sonar.tests", "src/test/kotlin")
            property("sonar.junit.reportPaths", "**/build/test-results/test/*.xml")
            property("sonar.junit.reportsPath", "$buildDir/test-results/test")
            property("detekt.sonar.kotlin.config.path", "detekt-config.yml")
            property("sonar.java.coveragePlugin", "jacoco")
            property("sonar.working.directory", "./.sonar")
            property("sonar.dynamicAnalysis", "reuseReport")
            property("sonar.sourceEncoding", "UTF-8")
        }
    }
}

allprojects {
    apply(plugin = "idea")
    group = "org.tired"
    version = "1.0.0-SNAPSHOT"
    repositories {
        jcenter()
        mavenLocal()
        mavenCentral()
    }

    jacoco {
        toolVersion = "0.8.4"
    }

    tasks.jacocoTestReport {
        reports {
            xml.isEnabled = true
            csv.isEnabled = false
            html.destination = file("$buildDir/jacocoHtml")
        }
    }

    dependencyCheck {
        format = ReportGenerator.Format.JUNIT
        cveValidForHours = 24
        failOnError = true
        failBuildOnCVSS = 4F
        suppressionFile = "owasp-suppressions.xml"
    }

    ktlint {
        version.set("0.36.0")
        debug.set(true)
        verbose.set(true)
        android.set(false)
        outputToConsole.set(true)
        reporters {
            reporter(ReporterType.PLAIN)
            reporter(ReporterType.CHECKSTYLE)
        }
        ignoreFailures.set(true)
        enableExperimentalRules.set(true)
        coloredOutput.set(true)
        additionalEditorconfigFile.set(file(".editorconfig"))
        kotlinScriptAdditionalPaths {
            include(fileTree("scripts/"))
        }
        filter {
            exclude("**/generated/**")
            include("**/kotlin/**")
        }
    }
}

tasks.getByName<Test>("test") {
    extensions.configure(JacocoTaskExtension::class) {
        isEnabled = true
        includes = listOf()
        excludes = listOf()
        excludeClassLoaders = listOf()
        isIncludeNoLocationClasses = true
        sessionId = "<auto-generated value>"
        isDumpOnExit = true
        classDumpDir = null
        output = JacocoTaskExtension.Output.FILE
        address = "localhost"
        port = 6300
        isJmx = false
    }
}

dependencies {
    newrelic("com.newrelic.agent.java:newrelic-agent:$newrelicVersion")
}

tasks.register<Copy>("copyNewRelic") {
    group = "build"
    description = "Add custom libraries to build our docker"

    from(configurations.get("newrelic").asPath)
    into("$buildDir/libs")
    rename { it.substring(0, it.indexOf("-")) + it.substring(it.lastIndexOf("."), it.length) }

    doLast {
        println("NewRelic copied!")
    }
}

tasks.getByName("assemble").dependsOn("copyNewRelic")
tasks.getByName("jacocoTestReport").dependsOn("test")

val allTestsCoverageFile = "$buildDir/jacoco/rootTestsCoverage.exec"

tasks.register<JacocoMerge>("jacocoMergeSubprojectResultsIntoRootOne") {
    onlyIf {
        project.childProjects.isNotEmpty()
    }
    destinationFile = file(allTestsCoverageFile)
    executionData = fileTree("dir" to ".", "include" to "**/build/jacoco/test.exec")
}

tasks.register<JacocoReport>("jacocoRootReport") {
    onlyIf {
        project.childProjects.isNotEmpty()
    }
    reports {
        xml.isEnabled = false
        csv.isEnabled = false
        html.isEnabled = true
        html.destination = file("$buildDir/jacocoHtml")
    }
    additionalSourceDirs.setFrom(files(subprojects.forEach { it.sourceSets.main.get().allSource.srcDirs }))
    sourceDirectories.setFrom(files(subprojects.forEach { it.sourceSets.main.get().allSource.srcDirs }))
    classDirectories.setFrom(files(subprojects.forEach { it.sourceSets.main.get().output }))
    executionData.setFrom(files("$buildDir/jacoco/*"))
}

tasks.getByName("jacocoRootReport").dependsOn("jacocoMergeSubprojectResultsIntoRootOne")
