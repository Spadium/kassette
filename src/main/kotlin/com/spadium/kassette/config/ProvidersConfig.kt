package com.spadium.kassette.config

import com.spadium.kassette.config.LibreSpotConfig
import com.spadium.kassette.config.serializers.IdentifierSerializer
import kotlinx.serialization.Serializable
import net.minecraft.util.Identifier

@Serializable
data class ProvidersConfig(
    @Serializable(with = IdentifierSerializer::class) var defaultProvider: Identifier,
    var spotify: SpotifyConfig,
    var librespot: LibreSpotConfig
)