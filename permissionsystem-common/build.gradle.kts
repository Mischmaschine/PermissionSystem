plugins {
    kotlin("jvm") version "1.7.10"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "de.permissionsystem"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}

dependencies {
    implementation("com.github.Mischmaschine:DatabaseLib:dev1.0-SNAPSHOT")
    implementation("com.google.code.gson:gson:2.9.0")
    implementation("org.mongodb:mongodb-driver-sync:4.6.0")
    compileOnly("org.mariadb.jdbc:mariadb-java-client:3.0.5")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}