plugins {
    id("java")
}

group = "me.clickism"
version = "$name-${property("pluginVersion")}"

base {
    archivesName.set(rootProject.name)
}

repositories {
    mavenCentral()
    mavenLocal()
    maven {
        name = "spigotmc-repo"
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.21.5-R0.1-SNAPSHOT")
}

val targetJavaVersion = 17
java {
    val javaVersion = JavaVersion.toVersion(targetJavaVersion)
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
    if (JavaVersion.current() < javaVersion) {
        toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
    }
}

tasks.processResources {
    val properties = mapOf(
        "version" to project.property("pluginVersion").toString()
    )

    filesMatching("plugin.yml") {
        expand(properties)
    }
    inputs.properties(properties)
}