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
    implementation(project(":client:services"))
    implementation(project(":client:storage"))
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}