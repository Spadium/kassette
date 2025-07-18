package com.spadium.kassette

import com.spadium.kassette.config.Config
import com.spadium.kassette.media.AccountMediaProvider
import com.spadium.kassette.media.AuthenticationCallbackServer
import com.spadium.kassette.media.MediaManager
import com.spadium.kassette.ui.MediaInfoHUD
import com.spadium.kassette.ui.screens.MediaInfoScreen
import com.spadium.kassette.ui.toasts.ErrorToast
import kotlinx.coroutines.runBlocking
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import org.lwjgl.glfw.GLFW
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.concurrent.thread


open class Kassette : ClientModInitializer {
    companion object {
        val logger: Logger = LoggerFactory.getLogger("Kassette")
        val errors: MutableMap<String, Throwable> = mutableMapOf()
    }

    override fun onInitializeClient() {
        try {
            AuthenticationCallbackServer().start()
        } catch (e: Exception) {
            logger.error("Error initializing Kassette's authentication callback!", e)
            errors.put("Authentication Callback Server", e)
        }
        Config.Instance = Config.reload()

        val openMediaInfoKeybind: KeyBinding = KeyBindingHelper.registerKeyBinding(
            KeyBinding(
                "key.kassette.info",
                InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_M,
                "category.kassette.kassette"
            )
        )

        ClientTickEvents.END_CLIENT_TICK.register(ClientTickEvents.EndTick { client: MinecraftClient? ->
            while (openMediaInfoKeybind.wasPressed()) {
                client!!.setScreen(MediaInfoScreen())
            }
        })

        ClientCommandRegistrationCallback.EVENT.register { dispatcher, registryAccess ->
            dispatcher.register(ClientCommandManager.literal("kassette").executes { ctx ->
                ctx.source.sendFeedback(Text.literal("Kassette command"))
                return@executes 1
            })
        }

        ClientLifecycleEvents.CLIENT_STARTED.register { client ->
            MediaInfoHUD()
            Config.addListener(MediaManager::onConfigChange)
            MediaManager.setProvider(Config.Instance.providers.defaultProvider)
            if (MediaManager.provider is AccountMediaProvider) {
                (MediaManager.provider as AccountMediaProvider).initiateLogin(true)
            }

            thread(name = "Kassette MediaManager Thread") {
                while (client.isRunning) {
                    try {
                        runBlocking {
                            MediaManager.provider.update()
                        }
                    } catch (e: Throwable) {
                        e.printStackTrace()
                        logger.warn("Exception thrown by provider for \"${MediaManager.provider.getServiceName()}\"! Using PlaceholderProvider as fallback!")
                        errors.put(
                            "${MediaManager.provider.getServiceName()} MediaProvider",
                            e
                        )
                        client.toastManager.add(ErrorToast("Error from ${MediaManager.provider.getServiceName()}!"))
                        MediaManager.setProvider(Identifier.of("kassette:placeholder"))
                    }
                }
                MediaManager.provider.destroy()
            }
        }
        logger.info("Locked and loaded")
    }
}