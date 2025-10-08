plugins {
    id("com.gradleup.shadow") version "8.3.3"
}

group = "net.vxoidpvp"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":api"))
    compileOnly("io.papermc.paper:paper-api:1.21-R0.1-SNAPSHOT")
}

tasks {
    processResources {
        filesMatching("plugin.yml") {
            expand("version" to project.version)
        }
    }

    shadowJar {
        archiveBaseName.set("${rootProject.name}-paper")
        archiveVersion.set(version.toString())
        archiveClassifier.set("")
    }

    build {
        dependsOn(shadowJar)
    }
}
