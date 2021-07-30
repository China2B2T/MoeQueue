plugins {
    java
    `maven-publish`
}

group = "org.china2b2t"
version = "1.0"

repositories {
    mavenCentral()

    maven { url = uri("https://jitpack.io") }
    maven { url = uri("https://gitlab.com/api/v4/projects/19978391/packages/maven") }
    maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") }
    maven { url = uri("http://repo.extendedclip.com/content/repositories/placeholderapi/") }
    maven { url = uri("https://repo.codemc.org/repository/maven-public") }
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
    maven { url = uri("https://repo.codemc.io/repository/nms/") }

}

dependencies {
    compileOnly("net.md-5:bungeecord-api:1.14-SNAPSHOT")
}

tasks.withType<ProcessResources> {
    include("**/*.yml")
    filter<org.apache.tools.ant.filters.ReplaceTokens>(
        "tokens" to mapOf(
            "VERSION" to project.version.toString()
        )
    )
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString();
            artifactId = project.name
            version = project.version.toString()

            from(components["java"])
        }
    }
}
