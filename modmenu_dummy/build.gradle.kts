plugins {
    id("java")
}

group = "com.terraformersmc.modmenu"
version = "stub"

tasks.jar {
    exclude("net/minecraft/client/gui/screens/Screen.class")
}