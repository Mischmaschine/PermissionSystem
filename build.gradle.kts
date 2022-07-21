import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    id("com.github.johnrengelman.shadow") version "7.0.0"
    kotlin("plugin.serialization") version "1.7.10"
}

allprojects {
    group = "de.permissionsystem"
    version = "1.0-SNAPSHOT"

    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "com.github.johnrengelman.shadow")
    apply(plugin = "org.jetbrains.kotlin.plugin.serialization")

    repositories {
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://repo.aikar.co/content/groups/aikar/")
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
        maven("https://nexus.velocitypowered.com/repository/maven-public/")
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }
    dependencies {
        implementation("com.github.Mischmaschine:DatabaseLib:master-SNAPSHOT")
        implementation("org.mongodb:mongodb-driver-sync:4.6.0")
        compileOnly("org.mariadb.jdbc:mariadb-java-client:3.0.5")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
        implementation("org.slf4j:slf4j-api:2.0.0-alpha7")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
    }

    tasks {
        build {
            dependsOn(shadowJar)
        }
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }
}

