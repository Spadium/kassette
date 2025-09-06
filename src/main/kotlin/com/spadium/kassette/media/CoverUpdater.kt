package com.spadium.kassette.media

import com.spadium.kassette.media.MediaManager.provider
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents
import net.minecraft.client.MinecraftClient
import net.minecraft.client.texture.NativeImageBackedTexture
import net.minecraft.util.Identifier

object CoverUpdater {
    private var coverArt: NativeImageBackedTexture = NativeImageBackedTexture(
        { "coverart" }, MediaManager.getDefaultCoverArt()
    )
    private var largeCoverArt: NativeImageBackedTexture = NativeImageBackedTexture(
        { "coverart_large" }, MediaManager.getDefaultCoverArt()
    )
    private val coverArtIdentifier = Identifier.of("kassette:coverart")
    private val largeCoverArtIdentifier = Identifier.of("kassette:coverart_large")

    @JvmStatic
    fun setupCoverArt() {
        if (
            provider.getMedia().coverArt != coverArt.image &&
            MinecraftClient.getInstance().textureManager != null
        ) {
            val textureManager = MinecraftClient.getInstance().textureManager
            val coverImage = MediaManager.provider.getMedia().coverArt
            coverArt.close()
            coverArt = NativeImageBackedTexture(
                { "coverart" }, coverImage
            )
            coverArt.setFilter(true, true)
            coverArt.upload()

            largeCoverArt.close()
            largeCoverArt = NativeImageBackedTexture(
                { "coverart_large" }, coverImage
            )
            largeCoverArt.setFilter(false, false)
            largeCoverArt.upload()

            textureManager.registerTexture(coverArtIdentifier, coverArt)
            textureManager.registerTexture(largeCoverArtIdentifier, largeCoverArt)
        }
    }
}