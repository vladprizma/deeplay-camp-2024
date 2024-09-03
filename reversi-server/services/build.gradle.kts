plugins {
    id("java")
    kotlin("jvm")
}

group = "io.deeplay.camp"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":reversi-server:model"))
    implementation(project(":utilities"))
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    implementation("org.postgresql:postgresql:42.3.1")
    implementation("org.json:json:20230227")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("ch.qos.logback:logback-classic:1.4.12")
    implementation("org.apache.httpcomponents.client5:httpclient5:5.1")
    implementation("org.mindrot:jbcrypt:0.4")
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly ("org.postgresql:postgresql")
    runtimeOnly ("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly ("io.jsonwebtoken:jjwt-jackson:0.11.5")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.3")
    implementation(kotlin("stdlib-jdk8"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}