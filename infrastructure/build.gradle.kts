import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val jacksonVersion = "2.10.0.pr2"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":model"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("com.github.kittinunf.fuel:fuel:2.2.1")
    implementation("com.github.kittinunf.fuel:fuel-jackson:2.2.1")
    implementation("com.amazonaws:aws-java-sdk-s3:${rootProject.extra.get("awsSDKVersion")}")
    implementation("redis.clients:jedis:3.1.0")
    api("com.zaxxer:HikariCP:3.4.2")
    api("com.github.seratch:kotliquery:1.3.1")
    api("mysql:mysql-connector-java:8.0.18")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-core:$jacksonVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    testImplementation("com.github.tomakehurst:wiremock-jre8:2.25.0")
    testImplementation(project(":model").dependencyProject.sourceSets.test.get().output)
    testImplementation("com.h2database:h2:1.4.200")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}
