// very janky because i prefer kotlin over groovy for these scripts and i rewrote them by hand
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
	id("fabric-loom")
	id("maven-publish")
	kotlin("jvm") version "2.1.21"
	kotlin("plugin.serialization") version "2.1.21"
}

version = property("mod_version")!!
group = property("maven_group")!!

base {
	// i wish there was a better way to get properties... this will do for now!
	archivesName = property("archives_base_name")!! as String
}

repositories {
	// Add repositories to retrieve artifacts from in here.
	// You should only use this when depending on other mods because
	// Loom adds the essential maven repositories to download Minecraft and libraries from automatically.
	// See https://docs.gradle.org/current/userguide/declaring_repositories.html
	// for more information about repositories.
	maven("https://jitpack.io")
	maven("https://maven.terraformersmc.com/")
	mavenCentral()
}

dependencies {
	// To change the versions see the gradle.properties file
	minecraft("com.mojang:minecraft:${property("minecraft_version")}")
	mappings("net.fabricmc:yarn:${property("yarn_mappings")}:v2")
	modImplementation("net.fabricmc:fabric-loader:${property("loader_version")}")

	// Fabric API. This is technically optional, but you probably want it anyway.
	modImplementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_version")}")
	modImplementation("net.fabricmc:fabric-language-kotlin:${property("fabric_kotlin_version")}")

	modImplementation("com.terraformersmc:modmenu:${property("modmenu_version")}")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")
	// temporary until i get to writing my own implementation
	implementation("com.adamratzman:spotify-api-kotlin-core:4.1.3")
}

tasks.processResources {
	val env = System.getenv()
	env.forEach { k, v ->
		println("$k : $v")
	}
	if (env["CI"] == "true") {
		logger.info("Running in CI!")
		inputs.property(
			"version",
			"${project.version}-${env.getOrDefault("GITHUB_SHA", "CI")}-${env.getOrDefault("GITHUB_REF_NAME", "GIT")}"
		)
	} else {
		inputs.property("version", project.version)
	}

	filesMatching("fabric.mod.json") {
		expand(
			mapOf("version" to inputs.properties["version"])
		)
	}
}

tasks.withType<JavaCompile>().configureEach {
	options.release = 21
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all {
	compilerOptions {
		jvmTarget = JvmTarget.JVM_21
	}
}

java {
	// Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
	// if it is present.
	// If you remove this line, sources will not be generated.
	withSourcesJar()

	sourceCompatibility = JavaVersion.VERSION_21
	targetCompatibility = JavaVersion.VERSION_21
}

tasks.jar {
	inputs.property("archivesName", project.base.archivesName)

	from("LICENSE") {
		rename { "${it}_${inputs.properties["archivesName"]}"}
	}
}

// configure the maven publication
publishing {
	publications {
		create<MavenPublication>("mavenJava") {
			artifactId = property("archives_base_name") as String
			from(components["java"])
		}
	}

	// See https://docs.gradle.org/current/userguide/publishing_maven.html for information on how to set up publishing.
	repositories {
		// Add repositories to publish to here.
		// Notice: This block does NOT have the same function as the block in the top level.
		// The repositories here will be used for publishing your artifact, not for
		// retrieving dependencies.
	}
}