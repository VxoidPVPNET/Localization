plugins {
    id("java")
    id("maven-publish")
}

allprojects {
    group = "net.vxoidpvp"
    version = "1.0.0"
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "maven-publish")

    repositories {
        maven("https://repo.papermc.io/repository/maven-public/")
        mavenCentral()
    }

    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }
    }
}