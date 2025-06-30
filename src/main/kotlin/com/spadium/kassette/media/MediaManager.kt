package com.spadium.kassette.media

import kotlinx.coroutines.delay
import net.minecraft.client.MinecraftClient

object MediaManager {
    var provider: MediaProvider = PlaceholderProvider()
    var info: MediaInfo = provider.getMedia()


}