package com.spadium.kassette.config

import kotlinx.serialization.Serializable

/*
    Config class
    The only values that are absolutely required are spotify, color, and borderColor
 */
@Serializable
data class Config(
    var spotify: SpotifyConfig,
    var color: Long,
    var borderColor: Long,
    var textSpeed: Float,
    var fancyTextSpeed: Float,
    var fancyText: Boolean,
    var callbackPort: UInt = 61008u,
    final var version: UInt = 0u
) {
    companion object {
        internal var Instance: Config = Config(
            SpotifyConfig("", ""),
            0L,
            0L,
            1f,
            5f,
            false,
        )

        fun getInstance(): Config {
            return Instance
        }
    }
}
