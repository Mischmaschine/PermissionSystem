plugins {
    kotlin("jvm") version "1.7.10"
    id("com.github.johnrengelman.shadow") version "7.0.0"
    kotlin("plugin.serialization") version "1.7.10"
}

group = "de.permissionsystem"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots") //BungeeCord-Repository
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
    compileOnly("net.md-5:bungeecord-api:1.19-R0.1-SNAPSHOT")
    implementation("co.aikar:acf-bungee:0.5.0-SNAPSHOT")
    compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}