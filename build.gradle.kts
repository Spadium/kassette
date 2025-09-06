// very janky because i prefer kotlin over groovy for these scripts and i rewrote them by hand
import org.gradle.internal.time.Time
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
	// very much not temporary, gives us more features than the web api
	implementation("xyz.gianlu.librespot:librespot-lib:1.6.5")
	// temporary until i get to writing my own implementation
	include(implementation("se.michaelthelin.spotify:spotify-web-api-java:9.2.0")!!)
	// required to not crash outside the dev environment
	include(implementation("org.apache.httpcomponents.client5:httpclient5:5.5")!!)
	include(implementation("org.apache.httpcomponents.client5:httpclient5-cache:5.4.3")!!)
	include(implementation("org.apache.httpcomponents.core5:httpcore5:5.3.4")!!)
	include(implementation("org.apache.httpcomponents.core5:httpcore5-h2:5.3.4")!!)
	include(implementation("com.neovisionaries:nv-i18n:1.29")!!)
}

sourceSets {
	main {
		kotlin {
			// exclude Kolor sources from build to save some space
			exclude("com/spadium/kassette/ui/kolor")
		}
	}
}

tasks.processResources {
	// makes sure that all properties are up-to-date
	outputs.upToDateWhen { false }
	val env = System.getenv()
	if (env["CI"] == "true") {
		if (env["GITHUB_EVENT_NAME"] != "release") {
			inputs.property(
				"version",
				"${project.version}-${env.getOrDefault("GITHUB_SHA", "CI")}-${env.getOrDefault("GITHUB_REF_NAME", "GIT")}"
			)
			inputs.property(
				"buildType",
				"CI"
			)
		} else {
			inputs.property(
				"version",
				project.version
			)
			inputs.property(
				"buildType",
				"RELEASE"
			)
		}

		inputs.property(
			"gitCommitId",
			env.getOrDefault("GITHUB_SHA", "N/A")
		)
		inputs.property(
			"gitBranchRef",
			env.getOrDefault("GITHUB_REF", "N/A")
		)
	} else {
		inputs.property("version", project.version)
		inputs.property("buildType", "DEV")
		inputs.property("gitCommitId", "N/A")
		inputs.property("gitBranchRef", "N/A")
	}

	filesMatching("fabric.mod.json") {
		expand(
			mapOf(
				"version" to inputs.properties["version"],
				"buildType" to inputs.properties.getOrDefault("buildType", "DEV"),
				"gitCommitId" to inputs.properties.getOrDefault("gitCommitId", "N/A"),
				"gitBranchRef" to inputs.properties.getOrDefault("gitBranchRef", "N/A"),
				"buildDate" to Time.currentTimeMillis()
			)
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