plugins {
    kotlin("jvm") version "1.7.10"
    id("com.github.johnrengelman.shadow") version "7.0.0"
    kotlin("plugin.serialization") version "1.7.10"
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
    implementation("org.mongodb:mongodb-driver-sync:4.6.0")
    compileOnly("org.mariadb.jdbc:mariadb-java-client:3.0.5")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}