plugins {
    id("java")
}

group = "com.terraformersmc.modmenu"
version = "1.0.0-alpha.1-experimental"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains:annotations:26.0.2")
}

tasks.test {
    useJUnitPlatform()
}