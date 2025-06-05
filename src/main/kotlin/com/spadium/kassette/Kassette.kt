package com.spadium.kassette

import com.spadium.kassette.ui.MediaInfoHUD
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import net.minecraft.text.Text
import org.lwjgl.glfw.GLFW
import org.slf4j.LoggerFactory


open class Kassette : ClientModInitializer {
	companion object {
		val logger = LoggerFactory.getLogger("Kassette")
	}

	override fun onInitializeClient() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		val openMediaInfoKeybind: KeyBinding = KeyBindingHelper.registerKeyBinding(KeyBinding(
				"key.kassette.info", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_P, "category.kassette.kassette"
		))

		ClientTickEvents.END_CLIENT_TICK.register(ClientTickEvents.EndTick { client: MinecraftClient? ->
			while (openMediaInfoKeybind.wasPressed()) {
				client!!.player!!.sendMessage(Text.literal("insert media info screen here!"), false)
				logger.debug("media screen is supposed to open by now")
			}
		})
		MediaInfoHUD().setup()

		logger.info("Hello Fabric world!")

		for (axis in 0..2) {
			logger.info("$axis")
		}
	}
}