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
    compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
    compileOnly("org.slf4j:slf4j-api:2.0.0-alpha7")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}