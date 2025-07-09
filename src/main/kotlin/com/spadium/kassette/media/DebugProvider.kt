package com.spadium.kassette.media

import com.spadium.kassette.Kassette
import kotlinx.coroutines.delay
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.client.texture.NativeImage
import net.minecraft.util.Identifier
import java.net.URI
import javax.net.ssl.HttpsURLConnection
import kotlin.math.floor

class DebugProvider: AccountMediaProvider {
    override var info = MediaInfo(
        60L, 0L, "TITLE", "ALBUM",
        "ARTIST", MediaManager.getDefaultCoverArt(), getServiceName(),
        MediaManager.MediaState.OTHER
    )
    private var f = 0f
    private var accumulator: Int = 0

    override fun initiateLogin() {
        Kassette.logger.debug("MEDIA PROVIDER INITIATE LOGIN")
    }

    override fun getServiceName(): String {
        return "Debug"
    }

    constructor() {
        Kassette.logger.debug("MEDIA PROVIDER INIT")
    }

    override fun destroy() {
        Kassette.logger.debug("MEDIA PROVIDER DESTROYED")
    }

    override suspend fun update() {
        delay(1000)
        accumulator++
        f += 0.25f
        if (f > 3) {
            f = 0f
        }
        if (info.currentPosition < info.maximumTime) {
            info.currentPosition++
        } else {
            info.currentPosition = 0
        }

        info.state = when (floor(f)) {
            0f -> MediaManager.MediaState.PLAYING
            1f -> MediaManager.MediaState.PAUSED
            2f -> MediaManager.MediaState.LOADING
            3f -> MediaManager.MediaState.OTHER
            else -> info.state
        }

        when (info.state) {
            MediaManager.MediaState.OTHER -> {
                info.coverArt = NativeImage.read(
                    MinecraftClient.getInstance().resourceManager
                        .open(Identifier.of("kassette", "textures/other_placeholder.png"))
                )
            }
            MediaManager.MediaState.PAUSED -> {
                try {
                    val url = URI("https://upload.wikimedia.org/wikipedia/commons/4/47/PNG_transparency_demonstration_1.png").toURL()
                    val uc: HttpsURLConnection = url.openConnection() as HttpsURLConnection
                    val inputStream = uc.inputStream
                    info.coverArt = NativeImage.read(
                        inputStream
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
            else -> {
                info.coverArt = MediaManager.getDefaultCoverArt()
            }
        }

        if (accumulator > 75) {
            throw NullPointerException("testing exception")
        }
        println("DEBUG UPDATE ${info.currentPosition}")
    }
}