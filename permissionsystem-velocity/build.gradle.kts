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
    maven("https://nexus.velocitypowered.com/repository/maven-public/")
    maven("https://repo.aikar.co/content/groups/aikar/")
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    compileOnly("com.velocitypowered:velocity-api:3.1.1")
    implementation(project(":permissionsystem-common"))
    implementation("co.aikar:acf-velocity:0.5.0-SNAPSHOT")
    implementation("net.kyori:adventure-text-minimessage:4.11.0")
    compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}