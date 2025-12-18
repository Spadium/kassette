package com.spadium.kassette

import com.mojang.blaze3d.platform.InputConstants
import com.spadium.kassette.config.Config
import com.spadium.kassette.config.ConfigMeta
import com.spadium.kassette.config.MainConfig
import com.spadium.kassette.media.AccountMediaProvider
import com.spadium.kassette.media.AuthenticationCallbackServer
import com.spadium.kassette.media.MediaManager
import com.spadium.kassette.ui.overlays.OverlayManager
import com.spadium.kassette.ui.screens.config.ConfigScreen
import com.spadium.kassette.ui.screens.media.ExtendedMediaInfoScreen
import com.spadium.kassette.ui.screens.media.MediaInfoScreen
import com.spadium.kassette.ui.toasts.ErrorToast
import com.spadium.kassette.util.ModNotification
import io.github.classgraph.ClassGraph
import io.github.classgraph.ScanResult
import kotlinx.coroutines.runBlocking
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import org.lwjgl.glfw.GLFW
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.concurrent.thread
import kotlin.reflect.KClass


open class Kassette : ClientModInitializer {
    companion object {
        val logger: Logger = LoggerFactory.getLogger("Kassette")
        val notifications: MutableList<ModNotification> = mutableListOf()
    }

    private val KassetteKeyCategory = KeyMapping.Category.register(Identifier.parse("kassette:kassette"))

    override fun onInitializeClient() {
        // Scan for classes with the ConfigMeta annotation
        logger.debug("Scanning for ConfigMeta annoations! Please wait...")
        val scanResult: ScanResult = ClassGraph().enableAnnotationInfo().scan()
        scanResult.getClassesWithAnnotation(ConfigMeta::class.java).forEach {
            Config.annotatedClassCache.add(it.loadClass(true).kotlin as? KClass<Config<*>> ?: error("Somehow, someway, not a Config<*> class!"))
        }

        try {
            AuthenticationCallbackServer().start()
        } catch (e: Exception) {
            logger.error("Error initializing Kassette's authentication callback!", e)
            notifications.add(
                ModNotification(
                    ModNotification.NotificationType.ERROR,
                    Component.literal("Authentication Callback Server"),
                    ModNotification.SourceType.MOD,
                    e
                )
            )
        }
        Config.reloadAll()

        val openMediaInfoKeybind: KeyMapping = KeyBindingHelper.registerKeyBinding(
            KeyMapping(
                "key.kassette.info",
                InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_M,
                KassetteKeyCategory
//                "category.kassette.kassette"
            )
        )

        ClientTickEvents.END_CLIENT_TICK.register(ClientTickEvents.EndTick { client: Minecraft? ->
            while (openMediaInfoKeybind.isDown) {
                client!!.setScreen(ExtendedMediaInfoScreen())
            }
        })

        ClientCommandRegistrationCallback.EVENT.register { dispatcher, registryAccess ->
            dispatcher.register(ClientCommandManager.literal("kassette")
                .then(ClientCommandManager.literal("reload").executes({ c ->
                    Config.reloadAll()
                    return@executes 1
                })).then(ClientCommandManager.literal("config").executes {
                    val client = Minecraft.getInstance()
                    client.execute {
                        val previousScreen = client.screen
                        client.setScreen(ConfigScreen(previousScreen))
                    }
                    return@executes 1;
                })
            )
        }

        ClientLifecycleEvents.CLIENT_STARTED.register { client ->
            OverlayManager
            MainConfig.addListener(MediaManager::onConfigChange)
            MediaManager.setProvider(MainConfig.Instance.providers.defaultProvider)
            if (MediaManager.provider is AccountMediaProvider) {
                (MediaManager.provider as AccountMediaProvider).initiateLogin(true)
            }

            thread(name = "Kassette MediaManager Thread") {
                while (client.isRunning) {
                    try {
                        runBlocking {
                            MediaManager.update()
                        }
                    } catch (e: Throwable) {
                        e.printStackTrace()
                        logger.warn("Exception thrown by provider for \"${MediaManager.provider.getServiceName()}\"! Using PlaceholderProvider as fallback!")
                        notifications.add(
                            ModNotification(
                                ModNotification.NotificationType.ERROR,
                                Component.literal("${MediaManager.provider.getServiceName()} MediaProvider"),
                                ModNotification.SourceType.PROVIDER,
                                e
                            )
                        )
                        client.toastManager.addToast(ErrorToast("Error from ${MediaManager.provider.getServiceName()}!"))
                        MediaManager.setProvider(Identifier.parse("kassette:placeholder"))
                    }
                }
                MediaManager.provider.destroy()
            }
        }
        logger.info("Locked and loaded")
    }
}