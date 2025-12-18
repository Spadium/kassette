pluginManagement {
	repositories {
		maven("https://maven.fabricmc.net/") {
			name = "Fabric"
		}
		mavenCentral()
		gradlePluginPortal()
	}
	val loom_version: String by settings

	plugins {
		id("net.fabricmc.fabric-loom-remap") version loom_version
	}
}