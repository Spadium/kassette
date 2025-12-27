plugins {
    id("java")
    id("net.fabricmc.fabric-loom")
}

group = "com.terraformersmc.modmenu"
version = "stub"

dependencies {
    minecraft("com.mojang:minecraft:${property("minecraft_version")}")
    implementation("net.fabricmc:fabric-loader:${property("loader_version")}")
}