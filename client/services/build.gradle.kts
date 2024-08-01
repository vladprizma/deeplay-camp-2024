plugins {
    id("java")
}

group = "io.deeplay.camp"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":utilities"))
    implementation(project(":client:utilitiesClient"))
    implementation(project(":client:storage"))
    implementation("org.jasypt:jasypt:1.9.3")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("ch.qos.logback:logback-classic:1.4.12")
}

tasks.test {
    useJUnitPlatform()
}