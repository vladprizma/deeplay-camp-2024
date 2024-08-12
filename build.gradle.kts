plugins {
    java
    id("net.ltgt.errorprone") version "4.0.1"
    jacoco
}

group = "io.deeplay.camp"
repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation(project(":reversi-server:controller"))
    implementation(project(":reversi-server:services"))
    implementation(project(":reversi-server:model"))
    implementation(project(":client"))
    implementation(project(":utilities"))
            
    errorprone("com.google.errorprone:error_prone_core:2.28.0")
    implementation("ch.qos.logback:logback-classic:1.4.12")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.14.0")
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        csv.required.set(false)
        html.required.set(false)
    }
}