plugins {
    kotlin("jvm") version "1.7.10"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "de.permissionsystem"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") //Spigot-Repository
    maven("https://jitpack.io")
    maven("https://repo.aikar.co/content/groups/aikar/")
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}

dependencies {
    implementation(project(":permissionsystem-common"))
    compileOnly("org.spigotmc:spigot-api:1.18-R0.1-SNAPSHOT")
    implementation("co.aikar:acf-paper:0.5.0-SNAPSHOT")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}