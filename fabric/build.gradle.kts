plugins {
	id("fabric-loom") version "1.10-SNAPSHOT"
}

version = "fabric-${project.property("mod_version")}+${project.property("minecraft_version")}"
group = project.property("maven_group").toString()

base {
	archivesName.set(property("archives_base_name").toString())
}

repositories {
}

dependencies {
	minecraft("net.minecraft:minecraft:${project.property("minecraft_version")}")
	mappings("net.fabricmc:yarn:${project.property("yarn_mappings")}:v2")
	modImplementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")
	modImplementation("net.fabricmc.fabric-api:fabric-api:${project.property("fabric_version")}")
}

tasks.processResources {
	inputs.property("version", project.version)

	filesMatching("fabric.mod.json") {
		expand("version" to inputs.properties["version"])
	}
}

java {
	val javaVersion = JavaVersion.VERSION_21
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(21))
	}
	sourceCompatibility = javaVersion
	targetCompatibility = javaVersion
}

tasks.jar {
	inputs.property("archivesName", project.base.archivesName.get())

	from("LICENSE") {
		rename { "${it}_${inputs.properties["archivesName"]}" }
	}
}