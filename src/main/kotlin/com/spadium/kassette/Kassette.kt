package com.spadium.kassette

import com.spadium.kassette.config.Config
import com.spadium.kassette.media.AuthenticationCallbackServer
import com.spadium.kassette.ui.MediaInfoHUD
import com.spadium.kassette.ui.MediaInfoScreen
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import net.minecraft.text.Text
import org.lwjgl.glfw.GLFW
import org.slf4j.LoggerFactory
import kotlin.io.path.exists
import kotlin.io.path.writeBytes


open class Kassette : ClientModInitializer {
	companion object {
		val logger = LoggerFactory.getLogger("Kassette")
	}

	val json: Json = Json {
		prettyPrint = true
		ignoreUnknownKeys = true
		encodeDefaults = true
	}

	@OptIn(ExperimentalSerializationApi::class)
    override fun onInitializeClient() {
		val openMediaInfoKeybind: KeyBinding = KeyBindingHelper.registerKeyBinding(KeyBinding(
			"key.kassette.info",
			InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_M,
			"category.kassette.kassette"
		))

		// Config stuff
		val configFile = FabricLoader.getInstance().configDir.resolve("kassette.json")

		if (configFile.exists()) {
			Config.Instance = json.decodeFromStream<Config>(
				configFile.toFile().inputStream()
			)
		} else {
			val jsonOut = json.encodeToString(Config.Instance)
			configFile.writeBytes(jsonOut.toByteArray())
		}

		ClientTickEvents.END_CLIENT_TICK.register(ClientTickEvents.EndTick { client: MinecraftClient? ->
			while (openMediaInfoKeybind.wasPressed()) {
				client!!.setScreen(
					MediaInfoScreen(Text.literal("blah blah blah"))
				)
			}
		})
		MediaInfoHUD().setup()

		logger.info("Locked and loaded")
		AuthenticationCallbackServer()
	}
}