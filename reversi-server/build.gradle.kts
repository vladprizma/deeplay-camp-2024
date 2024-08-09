plugins {
    id("java")
    id("org.javamodularity.moduleplugin") version "1.8.12"
    id("org.beryx.jlink") version "2.25.0"
}

group = "io.deeplay.camp"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}