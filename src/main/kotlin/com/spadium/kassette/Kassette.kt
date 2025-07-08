package com.spadium.kassette

import com.mojang.blaze3d.opengl.GlConst
import com.mojang.blaze3d.pipeline.RenderPipeline
import com.spadium.kassette.config.Config
import com.spadium.kassette.media.AccountMediaProvider
import com.spadium.kassette.media.AuthenticationCallbackServer
import com.spadium.kassette.media.DebugProvider
import com.spadium.kassette.media.MediaManager
import com.spadium.kassette.media.PlaceholderProvider
import com.spadium.kassette.media.spotify.SpotifyProvider
import com.spadium.kassette.ui.MediaInfoHUD
import com.spadium.kassette.ui.MediaInfoScreen
import kotlinx.coroutines.runBlocking
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gl.RenderPipelines
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import org.lwjgl.glfw.GLFW
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.imageio.ImageIO
import kotlin.concurrent.thread


open class Kassette : ClientModInitializer {
    companion object {
        val logger: Logger = LoggerFactory.getLogger("Kassette")
    }

    override fun onInitializeClient() {
        try {
            AuthenticationCallbackServer().start()
        } catch (e: Exception) {
            logger.error("Error initializing Kassette's authentication callback!", e)
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
                client!!.setScreen(
                    MediaInfoScreen(Text.translatable("kassette.popup.title"))
                )
            }
        })

        ClientCommandRegistrationCallback.EVENT.register { dispatcher, registryAccess ->
            dispatcher.register(ClientCommandManager.literal("kassette").executes { ctx ->
                ctx.source.sendFeedback(Text.literal("Kassette command"))
                return@executes 1
            })
        }

        ClientLifecycleEvents.CLIENT_STARTED.register { client ->
            MediaInfoHUD().setup()
            MediaManager.provider = SpotifyProvider()
            (MediaManager.provider as AccountMediaProvider).initiateLogin()
//            MediaManager.provider.init()
            thread(name = "Kassette MediaManager Thread") {
                while (client.isRunning) {
                    try {
                        runBlocking {
                            MediaManager.provider.update()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        logger.warn("Exception thrown by provider for \"${MediaManager.provider.getServiceName()}\"! Using PlaceholderProvider as fallback!")
                        MediaManager.provider = PlaceholderProvider()
                    }
                }
            }
        }
        logger.info("Locked and loaded")
    }
}