plugins {
    kotlin("jvm") version "1.7.22"
}

repositories {
    mavenCentral()
}

tasks {
    sourceSets {
        main {
            java.srcDirs("src")
        }
    }

    wrapper {
        gradleVersion = "7.6"
    }
}

dependencies {
    implementation("com.google.truth:truth:1.1.3")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
}