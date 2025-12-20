package me.spadium.kassette.media

import me.spadium.kassette.media.MediaManager.provider
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.texture.DynamicTexture
import net.minecraft.resources.Identifier

object CoverUpdater {
    private var coverArt: DynamicTexture = DynamicTexture(
        { "coverart" }, MediaManager.getDefaultCoverArt()
    )
    private var largeCoverArt: DynamicTexture = DynamicTexture(
        { "coverart_large" }, MediaManager.getDefaultCoverArt()
    )
    private val coverArtIdentifier = Identifier.parse("kassette:coverart")
    private val largeCoverArtIdentifier = Identifier.parse("kassette:coverart_large")

    @JvmStatic
    fun setupCoverArt() {
        if (
            provider.getMedia().coverArt != coverArt.pixels &&
            Minecraft.getInstance().textureManager != null
        ) {
            val textureManager = Minecraft.getInstance().textureManager
            val coverImage = MediaManager.provider.getMedia().coverArt
            coverArt.close()
            coverArt = DynamicTexture(
                { "coverart" }, coverImage
            )
//            coverArt.setFilter(true, true)
            coverArt.upload()

            largeCoverArt.close()
            largeCoverArt = DynamicTexture(
                { "coverart_large" }, coverImage
            )
//            largeCoverArt.setFilter(false, false)
            largeCoverArt.upload()

            textureManager.register(coverArtIdentifier, coverArt)
            textureManager.register(largeCoverArtIdentifier, largeCoverArt)
        }
    }
}