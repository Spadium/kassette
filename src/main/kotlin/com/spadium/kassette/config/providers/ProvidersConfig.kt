package com.spadium.kassette.config.providers

import com.spadium.kassette.config.serializers.IdentifierSerializer
import com.spadium.kassette.config.serializers.ResourceLocationSerializer
import kotlinx.serialization.Serializable
import net.minecraft.resources.ResourceLocation

@Serializable
data class ProvidersConfig(
    @Serializable(with = ResourceLocationSerializer::class)
    var defaultProvider: ResourceLocation,
    var spotify: SpotifyConfig,
    var librespot: LibreSpotConfig,
    var callbackPort: UInt = 61008u
)