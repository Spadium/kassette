package com.spadium.kassette

import com.spadium.kassette.media.AuthenticationCallbackServer
import com.spadium.kassette.ui.MediaInfoHUD
import com.spadium.kassette.ui.MediaInfoScreen
import kotlinx.serialization.ExperimentalSerializationApi
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import net.minecraft.text.Text
import org.lwjgl.glfw.GLFW
import org.slf4j.Logger
import org.slf4j.LoggerFactory


open class Kassette : ClientModInitializer {
	companion object {
		val logger: Logger = LoggerFactory.getLogger("Kassette")
	}

	@OptIn(ExperimentalSerializationApi::class)
    override fun onInitializeClient() {
		val openMediaInfoKeybind: KeyBinding = KeyBindingHelper.registerKeyBinding(KeyBinding(
			"key.kassette.info",
			InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_M,
			"category.kassette.kassette"
		))

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