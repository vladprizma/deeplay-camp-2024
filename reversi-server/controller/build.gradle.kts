plugins {
    id("java")
}

group = "io.deeplay.camp"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":reversi-server:model"))
    implementation(project(":utilities"))
    implementation(project(":reversi-server:services"))
    
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("ch.qos.logback:logback-classic:1.4.12")
    implementation("com.github.oshi:oshi-core:6.6.3")
}

tasks.test {
    useJUnitPlatform()
}