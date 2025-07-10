package com.spadium.kassette.config

import com.spadium.kassette.serializers.IdentifierSerializer
import kotlinx.serialization.Serializable
import net.minecraft.util.Identifier

@Serializable
data class ProvidersConfig(
    @Serializable(with = IdentifierSerializer::class) var defaultProvider: Identifier,
    var spotify: SpotifyConfig
)