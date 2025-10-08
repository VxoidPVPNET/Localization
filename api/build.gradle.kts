plugins {
    id("java-library")
    id("maven-publish")
}

group = "net.vxoidpvp"
version = "1.0.0"

dependencies {
    implementation("com.google.code.gson:gson:2.13.2")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            groupId = "net.vxoidpvp"
            artifactId = "localization-api"
            version = project.version.toString()
        }
    }
}
